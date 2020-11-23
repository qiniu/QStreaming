/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiniu.stream.sql.hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put}
import org.apache.hadoop.hbase.spark.{HBaseContext, LatestHBaseContextCache}
import org.apache.hadoop.hbase.spark.datasources.{HBaseSparkConf, HBaseTableCatalog, SerializableConfiguration, Utils}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.internal.Logging
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.sources.v2.writer.streaming.StreamWriter
import org.apache.spark.sql.sources.v2.writer.{DataWriter, DataWriterFactory, WriterCommitMessage}
import org.apache.spark.sql.types.StructType

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer


/**
 * Dummy commit message. The DataSourceV2 framework requires a commit message implementation but we
 * don't need to really send one.
 */
case object HBaseWriterCommitMessage extends WriterCommitMessage

/**
 * A [[org.apache.spark.sql.sources.v2.writer.streaming.StreamWriter]] for hbase writing.
 * Responsible for generating the writer factory.
 */
class HBaseStreamWriter
(
  schema: StructType,
  options: Map[String, String]
) extends StreamWriter with Logging {
  override def commit(epochId: Long, messages: Array[WriterCommitMessage]): Unit = {
    log.info(s"epoch ${epochId} of HBaseStreamWriter commited!")
  }

  override def abort(epochId: Long, messages: Array[WriterCommitMessage]): Unit = {
    log.info(s"epoch ${epochId} of HBaseStreamWriter aborted!")
  }

  override def createWriterFactory(): DataWriterFactory[InternalRow] = {
    new HBaseStreamWriterFactory(schema, options)
  }
}

/**
 * A [[DataWriterFactory]] for hbase writing.
 * Will be serialized and sent to executors to generate the per-task data writers.
 */
case class HBaseStreamWriterFactory(
                                     schema: StructType,
                                     options: Map[String, String]
                                   ) extends DataWriterFactory[InternalRow] with Logging {
  override def createDataWriter(
                                 partitionId: Int,
                                 taskId: Long,
                                 epochId: Long): DataWriter[InternalRow] = {
    log.info(s"Create date writer for TID ${taskId}, EpochId ${epochId}")
    HBaseStreamDataWriter(Option(schema), options)
  }
}

object HBaseConnection {

  private var _conn: Connection = _

  def getConnection(conf: Configuration) = {
    if (_conn == null) {
      _conn = ConnectionFactory.createConnection(conf)
    }
    _conn
  }
}

/**
 * A [[org.apache.spark.sql.sources.v2.writer.DataWriter]] for hbase writing.
 * One data writer will be created in each partition to process incoming rows.
 */
case class HBaseStreamDataWriter(
                                  userSpecifiedSchema: Option[StructType] = None,
                                  parameters: Map[String, String]
                                ) extends DataWriter[InternalRow] with Logging {


  val timestamp: Option[Long] = parameters.get(HBaseSparkConf.TIMESTAMP).map(_.toLong)

  val catalog = HBaseTableCatalog(parameters)

  def tableName: String = catalog.name

  val configResources: Option[String] = parameters.get(HBaseSparkConf.HBASE_CONFIG_LOCATION)

  val wrappedConf = {
    val cfg = new Configuration()
    configResources.foreach(resource => resource.split(",").foreach(r => cfg.addResource(r)))
    parameters.get(HConstants.ZOOKEEPER_CLIENT_PORT.toLowerCase).foreach(cfg.set(HConstants.ZOOKEEPER_CLIENT_PORT, _))
    parameters.get(HConstants.ZOOKEEPER_QUORUM.toLowerCase).foreach(cfg.set(HConstants.ZOOKEEPER_QUORUM, _))
    new SerializableConfiguration(cfg)
  }

  /**
   * Generates a Spark SQL schema objeparametersct so Spark SQL knows what is being
   * provided by this BaseRelation
   *
   * @return schema generated from the SCHEMA_COLUMNS_MAPPING_KEY value
   */
  val schema: StructType = catalog.toDataType

  // use a local cache for batch write to hbase.
  private val batchSize = parameters.get("batchSize").map(_.toInt).getOrElse(1000)
  private val localBuffer = new ArrayBuffer[Row](batchSize)

  override def write(record: InternalRow): Unit = {
    localBuffer.append(new GenericRowWithSchema(record.copy().toSeq(schema).toArray, schema))
    if (localBuffer.size == batchSize) {
      log.debug(s"Local buffer is full with size $batchSize, do write and reset local buffer.")
      doWriteAndResetBuffer()
    }
  }


  private def doWriteAndResetBuffer(): Unit = {
    val size = localBuffer.size
    try {
      val start = System.currentTimeMillis()
      val rkFields = catalog.getRowKey
      val rkIdxedFields = rkFields.map { case x =>
        (schema.fieldIndex(x.colName), x)
      }
      val colsIdxedFields = schema
        .fieldNames
        .partition(x => rkFields.map(_.colName).contains(x))
        ._2.map(x => (schema.fieldIndex(x), catalog.getField(x)))

      def convertToPut(row: Row): Put = {
        // construct bytes for row key
        val rowBytes = rkIdxedFields.map { case (x, y) =>
          Utils.toBytes(row(x), y)
        }
        val rLen = rowBytes.foldLeft(0) { case (x, y) =>
          x + y.length
        }
        val rBytes = new Array[Byte](rLen)
        var offset = 0
        rowBytes.foreach { x =>
          System.arraycopy(x, 0, rBytes, offset, x.length)
          offset += x.length
        }
        val put = timestamp.fold(new Put(rBytes))(new Put(rBytes, _))

        colsIdxedFields.foreach { case (x, y) =>
          val r = row(x)
          if (r != null) {
            val b = Utils.toBytes(r, y)
            put.addColumn(Bytes.toBytes(y.cf), Bytes.toBytes(y.col), b)
          }
        }
        put
      }

      val table = HBaseConnection.getConnection(wrappedConf.value).getTable(TableName.valueOf(tableName))
      table.put(localBuffer.map(convertToPut).toList.asJava)
      localBuffer.clear()
      if (table != null) {
        table.close()
      }
      log.debug(s"Success write $size records, cost ${System.currentTimeMillis() - start} ms")
    } catch {
      case e: Throwable =>
        log.error(s"Failed to write $size records, abort writing!", e)
        throw e
    }
  }

  private def doWriteAndClose(): Unit = {
    if (localBuffer.nonEmpty) {
      doWriteAndResetBuffer()
    }
  }

  override def commit(): WriterCommitMessage = {
    doWriteAndClose()
    HBaseWriterCommitMessage
  }

  override def abort(): Unit = {
    log.info(s"Abort writing with ${localBuffer.size} records in local buffer.")
  }
}
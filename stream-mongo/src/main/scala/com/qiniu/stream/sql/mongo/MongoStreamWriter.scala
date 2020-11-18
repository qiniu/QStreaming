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

package com.qiniu.stream.sql.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.model._
import com.mongodb.spark.MongoConnector
import com.mongodb.spark.config.WriteConfig
import com.mongodb.spark.sql.MongoMapFunctions
import org.apache.spark.internal.Logging
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.sources.v2.writer.streaming.StreamWriter
import org.apache.spark.sql.sources.v2.writer.{DataWriter, DataWriterFactory, WriterCommitMessage}
import org.apache.spark.sql.types.StructType
import org.bson.BsonDocument

import scala.collection.JavaConverters.{asScalaSetConverter, _}
import scala.collection.mutable.ArrayBuffer


/**
 * Dummy commit message. The DataSourceV2 framework requires a commit message implementation but we
 * don't need to really send one.
 */
case object MongoWriterCommitMessage extends WriterCommitMessage

/**
 * A [[org.apache.spark.sql.sources.v2.writer.streaming.StreamWriter]] for mongo writing.
 * Responsible for generating the writer factory.
 */
class MongoStreamWriter(
                         schema: StructType,
                         options: Map[String, String]
                       ) extends StreamWriter with Logging {
  override def commit(epochId: Long, messages: Array[WriterCommitMessage]): Unit = {
    log.info(s"epoch ${epochId} of MongoStreamWriter commited!")
  }

  override def abort(epochId: Long, messages: Array[WriterCommitMessage]): Unit = {
    log.info(s"epoch ${epochId} of MongoStreamWriter aborted!")
  }

  override def createWriterFactory(): DataWriterFactory[InternalRow] = {
    MongoStreamWriterFactory(schema, options)
  }
}

/**
 * A [[DataWriterFactory]] for mongo writing.
 * Will be serialized and sent to executors to generate the per-task data writers.
 */
case class MongoStreamWriterFactory(
                                     schema: StructType,
                                     options: Map[String, String]
                                   ) extends DataWriterFactory[InternalRow] with Logging {
  override def createDataWriter(
                                 partitionId: Int,
                                 taskId: Long,
                                 epochId: Long): DataWriter[InternalRow] = {
    log.info(s"Create date writer for TID ${taskId}, EpochId ${epochId}")
    MongoStreamDataWriter(schema, options)
  }
}

/**
 * A [[org.apache.spark.sql.sources.v2.writer.DataWriter]] for Mongo writing.
 * One data writer will be created in each partition to process incoming rows.
 */
case class MongoStreamDataWriter(
                                  schema: StructType,
                                  options: Map[String, String]
                                ) extends DataWriter[InternalRow] with Logging {

  private val writeConfig: WriteConfig = WriteConfig(options)
  // use a local cache for batch write to mongo.
  private val maxBatchSize = writeConfig.maxBatchSize
  private val localBuffer = new ArrayBuffer[Row](maxBatchSize)

  override def write(record: InternalRow): Unit = {
    localBuffer.append(new GenericRowWithSchema( record.copy().toSeq(schema).toArray,schema))
    if (localBuffer.size == maxBatchSize) {
      log.debug(s"Local buffer is full with size $maxBatchSize, do write and reset local buffer.")
      doWriteAndResetBuffer()
    }
  }

  // batch write to mongo, retry for SQLException
  private def doWriteAndResetBuffer(): Unit = {
    val queryKeyList = BsonDocument.parse(writeConfig.shardKey.getOrElse("{_id: 1}")).keySet().asScala.toList
    val mongoConnector = MongoConnector(writeConfig.asOptions)
    mongoConnector.withCollectionDo(writeConfig, { collection: MongoCollection[BsonDocument] =>
      val batch = localBuffer.map(MongoMapFunctions.rowToDocument)
      val requests = batch.map(doc =>
        if (queryKeyList.forall(doc.containsKey(_))) {
          val queryDocument = new BsonDocument()
          queryKeyList.foreach(key => queryDocument.append(key, doc.get(key)))
          if (writeConfig.replaceDocument) {
            new ReplaceOneModel[BsonDocument](queryDocument, doc, new ReplaceOptions().upsert(true))
          } else {
            queryDocument.keySet().asScala.foreach(doc.remove(_))
            new UpdateOneModel[BsonDocument](queryDocument, new BsonDocument("$set", doc), new UpdateOptions().upsert(true))
          }
        } else {
          new InsertOneModel[BsonDocument](doc)
        })
      collection.bulkWrite(requests.toList.asJava, new BulkWriteOptions().ordered(writeConfig.ordered))
    })
  }

  private def doWriteAndClose(): Unit = {
    if (localBuffer.nonEmpty) {
      doWriteAndResetBuffer()
    }
  }

  override def commit(): WriterCommitMessage = {
    doWriteAndClose()
    MongoWriterCommitMessage
  }

  override def abort(): Unit = {
    log.info(s"Abort writing with ${
      localBuffer.size
    } records in local buffer.")
  }

}
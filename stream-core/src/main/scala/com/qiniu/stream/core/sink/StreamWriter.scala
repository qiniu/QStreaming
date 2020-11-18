/*
 * Copyright 2020 Qiniu Cloud (qiniu.com)
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
package com.qiniu.stream.core.sink

import com.qiniu.stream.core.config.{Connector, SinkTable}
import com.qiniu.stream.core.util.StreamOptions
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.streaming.{DataStreamWriter, StreamingQuery, Trigger}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.storage.StorageLevel

class StreamWriter extends Writer with Logging {


  override def write(dataFrame: DataFrame,sinkTable: SinkTable): Unit = {

    if (sinkTable.connectors.size == 1)
      startOneStreamQuery(dataFrame, sinkTable.connectors.head,sinkTable)
    else
      startMultipleStreamQuery(dataFrame, sinkTable.connectors,sinkTable)

  }

  private def startMultipleStreamQuery(dataFrame: DataFrame, connectors: Seq[Connector],sinkTable: SinkTable): StreamingQuery = {
    val writer: DataStreamWriter[Row] = newStreamWriter(dataFrame,sinkTable)
    writer.foreachBatch((dataSet, batchId) => {
      val storageLevel = sinkTable.option("storageLevel").map(StorageLevel.fromString).getOrElse(StorageLevel.MEMORY_ONLY)
      dataSet.persist(storageLevel)
      connectors.par.foreach(writeBatch(dataSet, _, Some(batchId),sinkTable))
      dataSet.unpersist
    })
    writer.start()
  }

  private def writeBatch(dataFrame: Dataset[Row], connector: Connector, batchId: Option[Long],sinkTable: SinkTable): Unit = {
    var dataSet = connector.conditionExpr.map(dataFrame.where).getOrElse(dataFrame)
    dataSet = connector.includeColumns.map(dataSet.selectExpr(_: _*)).getOrElse(dataSet)
    dataSet = connector.excludeColumns.map(dataSet.drop(_: _*)).getOrElse(dataSet)
    if (connector.isConsole) {
      //only used for  debug
      dataSet.show()
    } else {
      val batchSinkTable = SinkTable(streaming = false, sinkTable.name, sinkTable.schema, sinkTable.format, Seq(connector))
      new BatchWriter().write(dataSet, batchId,batchSinkTable)
    }
  }

  private def startOneStreamQuery(dataFrame: DataFrame, connector: Connector,sinkTable: SinkTable): StreamingQuery = {

    val writer: DataStreamWriter[Row] = newStreamWriter(dataFrame,sinkTable)
    val batchWrite = sinkTable.option(StreamOptions.batchWrite).exists(_.toBoolean)
    if (batchWrite) {
      writer.foreachBatch((dataSet, batchId) => {
        writeBatch(dataSet, connector, Some(batchId),sinkTable)
      })
    } else {
      writer.format(connector.name).options(connector.options)
    }
    writer.start()
  }

  private def newStreamWriter(dataFrame: DataFrame,sinkTable: SinkTable): DataStreamWriter[Row] = {
    val writer = dataFrame.writeStream
    sinkTable.updateMode.foreach(writer.outputMode)
    sinkTable.partitions.foreach(writer.partitionBy(_: _*))
    withStreamOption(writer,sinkTable)
    writer
  }

  private def withStreamOption(writer: DataStreamWriter[Row],sinkTable: SinkTable): Unit = {
    sinkTable.option(StreamOptions.queryName).foreach(writer.queryName)
    sinkTable.option(StreamOptions.checkpointLocation).foreach(location => writer.option(StreamOptions.checkpointLocation, location))
    val trigger = (sinkTable.option(StreamOptions.triggerMode), sinkTable.option(StreamOptions.triggerInterval)) match {
      case (Some(StreamOptions.triggerModeProcessingTime), Some(interval)) => Some(Trigger.ProcessingTime(interval))
      case (Some(StreamOptions.triggerModeContinuous), Some(interval)) => Some(Trigger.Continuous(interval))
      case (Some(StreamOptions.triggerModeOnce), None) => Some(Trigger.Once())
      case _ => None
    }
    trigger.foreach(writer.trigger)
  }


}

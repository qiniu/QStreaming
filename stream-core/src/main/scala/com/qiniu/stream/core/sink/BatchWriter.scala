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
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.DataFrame

class BatchWriter extends Writer with Logging{

  override def write(dataFrame: DataFrame, sinkTable: SinkTable) = {
    write(dataFrame, None, sinkTable)
  }

  def write(dataFrame: DataFrame, batchId: Option[Long], sinkTable: SinkTable) = {
    if (sinkTable.connectors.size == 1)
      startOneBatch(dataFrame, sinkTable.connectors.head, batchId, sinkTable)
    else
      startMultipleBatch(dataFrame, sinkTable.connectors, batchId, sinkTable)
  }

  private def startMultipleBatch(dataFrame: DataFrame, connectors: Seq[Connector], batchId: Option[Long], sinkTable: SinkTable): Unit = {
    dataFrame.persist()
    connectors.foreach(startOneBatch(dataFrame, _, batchId, sinkTable))
    dataFrame.unpersist()
  }

  private def startOneBatch(dataFrame: DataFrame, connector: Connector, batchId: Option[Long], sinkTable: SinkTable): Unit = {
    logInfo(s"start batch for connector ${connector.name} with options ${connector.options.mkString(",")}")
    val writer = dataFrame.write.format(connector.name).options(connector.options)
    sinkTable.updateMode.orElse(connector.outputMode).foreach(writer.mode)
    sinkTable.partitions.orElse(connector.partitions).foreach(writer.partitionBy(_: _*))
    sinkTable.bucket.orElse(connector.buckets).foreach(bucket => writer.bucketBy(bucket.bucket, bucket.columns.head, bucket.columns.tail: _*))
    writer.save()
  }
}

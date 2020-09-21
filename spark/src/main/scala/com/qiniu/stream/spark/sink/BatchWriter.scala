package com.qiniu.stream.spark.sink

import com.qiniu.stream.spark.config.{Connector, SinkTable}
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

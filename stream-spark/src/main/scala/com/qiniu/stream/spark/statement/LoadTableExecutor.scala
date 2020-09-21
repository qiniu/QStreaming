package com.qiniu.stream.spark.statement

import com.qiniu.stream.spark.config.SourceTable
import com.qiniu.stream.spark.core.JobContext
import com.qiniu.stream.spark.source.{BatchReader, Reader, StreamReader}
import com.qiniu.stream.spark.util.DatasetUtils
import org.apache.spark.sql.SparkSession

case class LoadTableExecutor(table:SourceTable) extends StatementExecutor {
  override def execute(jobContext: JobContext, sparkSession: SparkSession): Unit = {
    val reader = if (table.connector.isCustom) {
      require(table.connector.reader.isDefined,"reader is required when using custom format")
      Class.forName(table.connector.reader.get).newInstance().asInstanceOf[Reader]
    } else {
      if (!table.streaming)
        new BatchReader
      else {
        new StreamReader
      }
    }
    val dataFrame = reader.read(sparkSession, table)
    dataFrame.createOrReplaceTempView(table.name)
    if (table.showSchema) {
      dataFrame.printSchema()
    }
    if (table.showTable) {
      DatasetUtils.showTable(dataFrame)
    }
    jobContext.setDebug(table.inDebugMode())
  }
}

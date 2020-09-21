package com.qiniu.stream.spark.statement

import com.qiniu.stream.core.job.JobContext
import com.qiniu.stream.spark.config.{InsertStatement, SinkTable}
import com.qiniu.stream.spark.sink.{BatchWriter, StreamWriter}
import com.qiniu.stream.spark.util.DatasetUtils
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

case class WriteTableExecutor(insertTable: InsertStatement) extends StatementExecutor with Logging {

  override def execute(jobContext: JobContext, sparkSession: SparkSession): Unit = {
    log.info(s"parsing insert statement ${insertTable.sql}")
    val dataFrame = sparkSession.sql(insertTable.sql)
    write(insertTable.sinkTable,dataFrame)
  }

  private def write(table:SinkTable, dataFrame: DataFrame) = {
    if (table.showTable || table.showSchema) {
      if (table.showSchema) {
        dataFrame.printSchema()
      }
      if (table.showTable) {
        DatasetUtils.showTable(dataFrame)
      }
    }else{
      val writer = if (table.streaming)
        new StreamWriter
      else
        new BatchWriter
      writer.write(dataFrame, table)
    }
  }

}

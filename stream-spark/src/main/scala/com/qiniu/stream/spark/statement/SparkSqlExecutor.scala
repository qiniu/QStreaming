package com.qiniu.stream.spark.statement

import com.qiniu.stream.spark.config.SqlStatement
import com.qiniu.stream.spark.core.JobContext
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.SparkSession

case class SparkSqlExecutor(sqlStatement:SqlStatement) extends StatementExecutor with Logging{
  override def execute(jobContext :JobContext, sparkSession: SparkSession): Unit = {
    logInfo(s"execute spark sql: ${sqlStatement.sql}")
    sparkSession.sql(sqlStatement.sql)
  }
}

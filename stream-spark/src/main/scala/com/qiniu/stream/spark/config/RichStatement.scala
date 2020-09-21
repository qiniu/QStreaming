package com.qiniu.stream.spark.config

import com.qiniu.stream.spark.core.JobContext
import com.qiniu.stream.spark.statement._
import org.apache.spark.sql.SparkSession

object RichStatement {

  implicit class RichStatement(statement: Statement) {
    def execute(jobContext: JobContext, sparkSession: SparkSession): Unit = {
      val executor = statement match {
        case sourceTable: SourceTable=>
          Some(LoadTableExecutor(sourceTable))
        case sqlQuery: CreateViewStatement =>
          Some(CreateViewExecutor(sqlQuery))
        case insertTable: InsertStatement =>
          Some(WriteTableExecutor(insertTable))
        case createFunctionStatement: CreateFunctionStatement =>
          Some(CreateFunctionExecutor(createFunctionStatement))
        case sqlStatement: SqlStatement =>
          Some(SparkSqlExecutor(sqlStatement))
        case _=> None
      }
      if (!jobContext.isDebugMode) {
        executor.foreach(_.execute(jobContext,sparkSession))
      }
      jobContext.setDebug(statement.inDebugMode())
    }
  }

}


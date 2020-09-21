package com.qiniu.stream.spark.statement

import com.qiniu.stream.spark.core.JobContext
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.SparkSession

trait StatementExecutor extends Logging{
  def execute(jobContext :JobContext, sparkSession: SparkSession)
}

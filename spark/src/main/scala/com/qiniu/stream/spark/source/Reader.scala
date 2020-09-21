package com.qiniu.stream.spark.source

import com.qiniu.stream.spark.config.SourceTable
import org.apache.spark.sql.{DataFrame, SparkSession}

trait Reader {
  def read(sparkSession: SparkSession,sourceTable:SourceTable): DataFrame
}


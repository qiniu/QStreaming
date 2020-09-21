package com.qiniu.stream.spark.source

import com.qiniu.stream.spark.config.RichSchema._
import com.qiniu.stream.spark.config.SourceTable
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}

class BatchReader extends Reader with Logging {

  override def read(sparkSession: SparkSession, source: SourceTable): DataFrame = {
    val reader: DataFrameReader = sparkSession.read
    val connector = source.connector
    reader.format(connector.name).options(connector.options)

    source.schema.foreach(schema=> reader.schema(schema.structType))
    reader.load()
  }


}

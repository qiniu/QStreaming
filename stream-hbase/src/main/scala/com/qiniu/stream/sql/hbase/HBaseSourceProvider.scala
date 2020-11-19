package com.qiniu.stream.sql.hbase
import org.apache.spark.sql.sources.DataSourceRegister
import org.apache.spark.sql.sources.v2.writer.streaming.StreamWriter
import org.apache.spark.sql.sources.v2.{DataSourceOptions, StreamWriteSupport}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.StructType

import scala.collection.JavaConverters.mapAsScalaMapConverter


class HBaseSourceProvider extends StreamWriteSupport with DataSourceRegister{
  override def createStreamWriter(queryId: String, schema: StructType, mode: OutputMode, options: DataSourceOptions): StreamWriter ={
    val optionMap = options.asMap().asScala.toMap
    new HBaseStreamWriter(schema,optionMap)
  }

  override def shortName(): String = "streaming-hbase"
}
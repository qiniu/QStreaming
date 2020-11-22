package com.qiniu.stream.core.source.redis

import com.qiniu.stream.core.config.SourceTable
import com.qiniu.stream.core.source.{Reader, WaterMarker}
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

class RedisStreamReader  extends Reader with WaterMarker with Logging {

  override def read(sparkSession: SparkSession, sourceTable: SourceTable): DataFrame = {
    require(sourceTable.schema.isDefined, "schema  is required")
    sparkSession
      .readStream
      .format("redis")
      .options(sourceTable.connector.options)
      .schema(sourceTable.schema.get.structType)
      .load()

  }


}

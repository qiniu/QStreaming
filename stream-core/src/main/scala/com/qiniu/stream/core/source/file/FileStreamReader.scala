package com.qiniu.stream.core.source.file

import com.qiniu.stream.core.config.SourceTable
import com.qiniu.stream.core.source.{Reader, WaterMarker}
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

class FileStreamReader extends Reader with WaterMarker with Logging {


  override def read(sparkSession: SparkSession,sourceTable: SourceTable): DataFrame = {
    require(sourceTable.schema.isDefined, "schema  is required")
    sparkSession
      .readStream.
      format(sourceTable.connector.name)
      .options(sourceTable.connector.options)
      .schema(sourceTable.schema.get.structType)
      .load()
  }


}

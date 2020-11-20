package com.qiniu.stream.core.source.kafka

import com.qiniu.stream.core.config.{RowFormat, RowTime, SourceTable}
import com.qiniu.stream.core.listener.KafkaLagListener
import com.qiniu.stream.core.source.{Reader, WaterMarker}
import com.qiniu.stream.core.util.Regex2Json
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.{DataFrame, SparkSession, functions => F}

class KafkaStreamReader extends Reader with WaterMarker with Logging {
  private val kafkaRawFields = List("key", "partition", "offset", "timestamp", "timestampType", "topic")
  private val kafkaBootstrapServers = "kafka.bootstrap.servers"

  private def jsonTable(table: DataFrame,sourceTable: SourceTable) = {
    val kafkaFields = table.schema.fieldNames.filterNot(_ == "value")
    table.withColumn("value", table.col("value").cast(DataTypes.StringType))
      .withColumn("value", F.from_json(F.col("value"), schema = sourceTable.schema.get.structType))
      .select("value.*", kafkaFields: _*)

  }

  private def avroTable(avroFormat: RowFormat, table: DataFrame) = {
    import org.apache.spark.sql.avro._
    val kafkaFields = table.schema.fieldNames.filterNot(_ == "value")
    require(avroFormat.props.contains("jsonSchema"), "jsonSchema is required for avro row format")
    val jsonSchema = avroFormat.props("jsonSchema")
    table.withColumn("value", from_avro(table.col("value"), jsonSchema)).select("value.*", kafkaFields: _*)

  }

  private def csvTable(csvFormat: RowFormat, table: DataFrame) = {
    table.withColumn("value", table.col("value").cast(DataTypes.StringType))
  }

  override def read(sparkSession: SparkSession,sourceTable: SourceTable): DataFrame = {
    require(sourceTable.schema.isDefined, "schema  is required")
    var table = sparkSession.readStream.format(sourceTable.connector.name).options(sourceTable.connector.options).load()
    enableKafkaLagListener(sparkSession,sourceTable)
    table = sourceTable.format match {
      case format if format.isJsonFormat =>
        jsonTable(table,sourceTable)
      case format if format.isAvroFormat =>
        avroTable(format, table)
      case format if format.isCsvFormat =>
        csvTable(format, table)
      case format if format.isRegExFormat=>
        regexTable(sparkSession,table, sourceTable)
      case format if format.isTextFormat=>
        table
      case _ =>
        jsonTable(table,sourceTable)
    }

    table = sourceTable.schema.get.timeField match {
      case Some(rowTime: RowTime) =>
        withWaterMark(table, Some(rowTime))
      case _ => table
    }
    table
  }


  private def regexTable(sparkSession: SparkSession,table:DataFrame,sourceTable: SourceTable) = {
    val regexPattern = sourceTable.format.props.get("pattern")
    require(regexPattern.isDefined, "regex pattern is required")
    val ddl = sourceTable.schema.get.toDDL
    val structType = sourceTable.schema.get.structType
    val pattern = regexPattern.get
    sparkSession.udf.register("regex2Json", F.udf((line: String) => {
      Regex2Json.toJson(line, pattern, ddl)
    }))

    table.withColumn("kafkaValue", F.struct(kafkaRawFields .map(F.col): _*))
      .selectExpr("regex2json(CAST(value AS STRING)) as jsonValue", "kafkaValue")
      .withColumn("value", F.from_json(F.col("jsonValue"), schema = structType))
      .select("value.*", "kafkaValue")
  }

  private def enableKafkaLagListener(sparkSession: SparkSession,sourceTable: SourceTable): Unit = {
    sourceTable.connector.option("group_id").foreach(groupId => {
      val bootStrapServer = sourceTable.connector.option(kafkaBootstrapServers)
      require(bootStrapServer.isDefined)
      log.info("register streaming query listener for kafka streaming")
      sparkSession.streams.addListener(new KafkaLagListener(groupId, bootStrapServer.get))
    })
  }
}

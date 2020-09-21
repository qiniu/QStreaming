package org.apache.spark.sql.execution.streaming.phoenix

import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, StreamSinkProvider}
import org.apache.spark.sql.streaming.OutputMode


class PhoenixDataSource extends DefaultSource with DataSourceRegister with StreamSinkProvider {
  override def shortName(): String = "phoenix"

  override def createSink(sqlContext: SQLContext,
                          parameters: Map[String, String],
                          partitionColumns: Seq[String],
                          outputMode: OutputMode): Sink = {
    new PhoenixSink(parameters, outputMode)
  }

  override def createRelation(sqlContext: SQLContext,
                              mode: SaveMode,
                              parameters: Map[String, String],
                              data: DataFrame): BaseRelation = {
    require(!(parameters.contains("include-columns") && parameters.contains("exclude-columns")),
      s"phoenix sink can not defined include-columns and exclude-columns parameters at the same time")


    super.createRelation(sqlContext, mode, parameters, data)
  }
}

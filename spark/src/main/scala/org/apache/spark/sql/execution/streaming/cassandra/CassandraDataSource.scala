package org.apache.spark.sql.execution.streaming.cassandra

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.sources.v2.DataSourceV2
import org.apache.spark.sql.sources.{DataSourceRegister, StreamSinkProvider}
import org.apache.spark.sql.streaming.OutputMode

class CassandraDataSource extends DataSourceV2 with StreamSinkProvider with DataSourceRegister {

  override def shortName(): String = "cassandra"

  override def createSink(sqlContext: SQLContext, parameters: Map[String, String], partitionColumns: Seq[String], outputMode: OutputMode): Sink = {
    new CassandraSink(sqlContext, parameters, partitionColumns, outputMode)
  }
}
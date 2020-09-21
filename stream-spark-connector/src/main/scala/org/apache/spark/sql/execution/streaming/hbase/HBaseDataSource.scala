package org.apache.spark.sql.execution.streaming.hbase

import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.sources.{DataSourceRegister, StreamSinkProvider}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, SQLContext}


class HBaseDataSource extends DefaultSource with DataSourceRegister with StreamSinkProvider {

  override def createSink(sqlContext: SQLContext,
                          parameters: Map[String, String],
                          partitionColumns: Seq[String], outputMode: OutputMode): Sink = {
    new HBaseSink(parameters, outputMode)
  }

  override def shortName: String = "hbase"
}

class HBaseSink(options: Map[String, String],
                outputMode: OutputMode) extends Sink {
  override def addBatch(batchId: Long,
                        data: DataFrame): Unit = {
    val relation = HBaseRepository(options)
    relation.writer(data, OutputMode.Update() == outputMode)
  }
}
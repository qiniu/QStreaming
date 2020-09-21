package org.apache.spark.sql.execution.streaming.cassandra

import com.qiniu.stream.util.Logging
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Row, SQLContext}


class CassandraSink(sqlContext: SQLContext, parameters: Map[String, String], partitionColumns: Seq[String], outputMode: OutputMode) extends Sink with Logging {
  log.debug(s"Initializing ${this.getClass.getSimpleName}")

  override def addBatch(batchId: Long, data: DataFrame): Unit = {

    val res = data.queryExecution.toRdd.mapPartitions { rows =>
      val converter = CatalystTypeConverters.createToScalaConverter(data.schema)
      rows.map(converter(_).asInstanceOf[Row])
    }
    val df = data.sparkSession.createDataFrame(res, data.schema)

    df.write.format("org.apache.spark.sql.cassandra").options(parameters).save()

  }
}


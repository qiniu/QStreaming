package com.qiniu.stream.spark.sink

import com.qiniu.stream.spark.config.SinkTable
import org.apache.spark.sql.DataFrame


trait Writer {
  def write(dataFrame: DataFrame,sinkTable: SinkTable)
}

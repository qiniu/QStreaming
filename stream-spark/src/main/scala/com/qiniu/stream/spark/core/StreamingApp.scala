package com.qiniu.stream.spark.core

import com.qiniu.stream.util.ParameterTool

import scala.collection.JavaConverters._
object StreamingApp {

  def main(args: Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args).toMap.asScala.toMap
    val jobOperator = JobOperator(params)
    try {
      jobOperator.start()
    } finally {
      jobOperator.stop()
    }
  }
}

package com.qiniu.stream.core

import com.qiniu.stream.core.util.ParameterTool
import collection.JavaConverters._
object StreamingApp {

  def main(args: Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args).toMap.asScala.toMap
    val runner = PipelineRunner(params)
    try {
      runner.start()
    } finally {
      runner.stop()
    }
  }
}

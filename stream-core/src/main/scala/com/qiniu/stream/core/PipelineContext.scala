package com.qiniu.stream.core

import com.qiniu.stream.core.config.{DebugEnabled, Settings}
import com.qiniu.stream.util.Logging
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

case class PipelineContext(settings: Settings) extends Logging {

  lazy val sparkSession: SparkSession = {
    val sparkConf: SparkConf = {
      val sparkConf = new SparkConf()
      if (settings.config.hasPath("spark")) {
        import scala.collection.JavaConversions._
        settings.config.getConfig("spark").entrySet().foreach(e => {
          sparkConf.set(e.getKey, settings.config.getString("spark." + e.getKey))
        })
      }
      sparkConf
    }
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
  }

  def stop = {
    Try {
      sparkSession.stop
    } match {
      case Success(_) =>
      case Failure(e) => logError("unexpected error while shutdown", e)
    }
  }

  def debug: Boolean = settings(DebugEnabled)

  def withDebug(debug: Boolean): Settings = settings.withValue(DebugEnabled, debug)

}
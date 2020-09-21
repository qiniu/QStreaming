package com.qiniu.stream.spark.core

import java.io.File

import com.qiniu.stream.spark.core.JobConfig.RichConfig
import com.qiniu.stream.util.Logging
import com.typesafe.config.{Config, ConfigFactory}
import org.antlr.v4.runtime.{CharStream, CharStreams}
import org.apache.spark.sql.SparkSession
import org.stringtemplate.v4.ST

import scala.util.{Failure, Success, Try}

/**
 * Default implementation of Spark Job Operator
 *
 */
case class JobOperator(config: Config,
                       params: Map[String, String] = Map(),
                       session: Option[SparkSession] = None,
                       jobString: Option[CharStream] = None
                           )   extends Logging {

  private val sparkSession = session.getOrElse(SparkSessionFactory.load(config))

  sys.addShutdownHook(stop())

  def start(): Unit = {
    val job = Job(jobContent, params)
    val jobContext = JobContext(job, config)

    def awaitTermination() {
      if (sparkSession.streams.active.nonEmpty) {
        if (isDebugEnable || jobContext.isDebugMode) {
          sparkSession.streams.active.foreach(_.processAllAvailable())
        } else {
          sparkSession.streams.awaitAnyTermination()
        }
      }
    }

    import com.qiniu.stream.spark.config.RichStatement._
    job.statements.foreach(_.execute(jobContext, sparkSession))
    awaitTermination()
  }

  def stop(): Unit = {
    Try {
      sparkSession.stop()
    } match {
      case Success(_) =>
      case Failure(e) => logError("unexpected error while shutdown", e)
    }
  }



  private val jobContent = {
    jobString.getOrElse {
      val source = config.source
      try {
        if (config.isTemplateEnable) {
          CharStreams.fromString(source.mkString)
        } else {
          val (startChar, stopChar) = config.templateStartStopChar
          val stTemplate = new ST(source.mkString, startChar, stopChar)
          params.foreach {
            case (k, v) => stTemplate.add(k, v)
          }
          val rendered = stTemplate.render()
          CharStreams.fromString(rendered)
        }
      } finally {
        source.close()
      }
    }
  }

  def isDebugEnable: Boolean = config.isDebugEnable
}

object JobOperator {

  def apply(params: Map[String, String]): JobOperator = {

    val config = params.get("config.file") match {
      case Some(file) => ConfigFactory.parseFile(new File(file))
      case None => ConfigFactory.load()
    }

    val jobDsl = params.get("sql.file") match {
      case Some(file) => Some(CharStreams.fromFileName(file))
      case None => None
    }

    val newParams = params - ("config.file", "sql.file")

    JobOperator(config, newParams, None, jobDsl)

  }

  def apply(config: Config, session: SparkSession, jobString: String): JobOperator = {
    require(config != null)
    require(session != null)
    require(jobString != null)
    JobOperator(config, Map(), Some(session), Some(CharStreams.fromString(jobString)))
  }

}




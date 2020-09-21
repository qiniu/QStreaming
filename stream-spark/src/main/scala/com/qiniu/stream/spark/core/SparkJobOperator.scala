package com.qiniu.stream.spark.core

import java.io.File

import com.qiniu.stream.util.Logging
import com.typesafe.config.{Config, ConfigFactory}
import org.antlr.v4.runtime.{CharStream, CharStreams}
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/**
 * Default implementation of Spark Job Operator
 *
 */
case class SparkJobOperator(config: Config,
                            params: Map[String, String] = Map(),
                            session: Option[SparkSession] = None,
                            jobString: Option[CharStream] = None
                           )  extends JobOperator(config, jobString, params) with Logging {

  private val sparkSession = session.getOrElse(SparkSessionFactory.load(config))

  sys.addShutdownHook(stop())


  override def stop(): Unit = {
    Try {
      sparkSession.stop()
    } match {
      case Success(_) =>
      case Failure(e) => logError("unexpected error while shutdown", e)
    }
  }

  protected override def startJob(job: Job): Unit = {
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

  override protected def parseJob(content: CharStream,
                                  params: Map[String, String]): Job =
    Job(content, params)
}

object SparkJobOperator {

  def apply(params: Map[String, String]): SparkJobOperator = {

    val config = params.get("config.file") match {
      case Some(file) => ConfigFactory.parseFile(new File(file))
      case None => ConfigFactory.load()
    }

    val jobDsl = params.get("sql.file") match {
      case Some(file) => Some(CharStreams.fromFileName(file))
      case None => None
    }

    val newParams = params - ("config.file", "sql.file")

    SparkJobOperator(config, newParams, None, jobDsl)

  }

  def apply(config: Config, session: SparkSession, jobString: String): SparkJobOperator = {
    require(config != null)
    require(session != null)
    require(jobString != null)
    SparkJobOperator(config, Map(), Some(session), Some(CharStreams.fromString(jobString)))
  }
}




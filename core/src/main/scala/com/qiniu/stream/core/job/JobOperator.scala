package com.qiniu.stream.core.job

import com.qiniu.stream.util.Logging
import com.typesafe.config.Config
import org.antlr.v4.runtime.{CharStream, CharStreams}
import org.stringtemplate.v4.ST

import scala.io.Source

abstract class JobOperator(config: Config, jobString: Option[CharStream] = None, params: Map[String, String] = Map()) extends Logging {
  import JobConfig._

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

  private val job = parseJob(jobContent,params)

  sys.addShutdownHook(stop())

  def stop(): Unit = {}

  def start(): Unit = {
    startJob(job)
  }

  protected def parseJob(content: CharStream, params: Map[String, String]): Job

  protected def startJob(job: Job): Unit

  def isDebugEnable: Boolean = config.isDebugEnable

}

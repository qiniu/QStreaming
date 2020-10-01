/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qiniu.stream.core

import java.io.File

import com.qiniu.stream.core.PipelineConfig.{PIPELINE_CONF, PIPELINE_DSL, RichConfig}
import com.qiniu.stream.util.Logging
import com.typesafe.config.{Config, ConfigFactory}
import org.antlr.v4.runtime.{CharStream, CharStreams}
import org.apache.spark.sql.SparkSession
import org.stringtemplate.v4.ST

import scala.util.{Failure, Success, Try}

case class PipelineRunner(config: Config,
                          params: Map[String, String] = Map(),
                          session: Option[SparkSession] = None,
                          jobString: Option[CharStream] = None
                         ) extends Logging {

  private val sparkSession = session.getOrElse(SparkSessionFactory.load(config))

  sys.addShutdownHook(stop())

  def start(): Unit = {
    val job = PipelineListener(jobContent, params)
    val jobContext = PipelineContext(job, config)

    def awaitTermination() {
      if (sparkSession.streams.active.nonEmpty) {
        if (isDebugEnable || jobContext.isDebugMode) {
          sparkSession.streams.active.foreach(_.processAllAvailable())
        } else {
          sparkSession.streams.awaitAnyTermination()
        }
      }
    }

    import com.qiniu.stream.core.config.RichStatement._
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

object PipelineRunner {


  def apply(params: Map[String, String]): PipelineRunner = {

    val config = params.get(PIPELINE_CONF) match {
      case Some(file) => ConfigFactory.parseFile(new File(file))
      case None => ConfigFactory.load()
    }

    val jobDsl = params.get(PIPELINE_DSL) match {
      case Some(file) => Some(CharStreams.fromFileName(file))
      case None => None
    }

    val newParams = params - (PIPELINE_CONF, PIPELINE_DSL)

    PipelineRunner(config, newParams, None, jobDsl)

  }

  def apply(config: Config, session: SparkSession, jobString: String): PipelineRunner = {
    require(config != null)
    require(session != null)
    require(jobString != null)
    PipelineRunner(config, Map(), Some(session), Some(CharStreams.fromString(jobString)))
  }

}




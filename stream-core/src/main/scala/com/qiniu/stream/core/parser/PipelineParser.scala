/*
 * Copyright 2020 Qiniu Cloud (qiniu.com)
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
package com.qiniu.stream.core.parser

import java.io.InputStream

import com.qiniu.stream.core.config._
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStream, CharStreams, CommonTokenStream}
import org.stringtemplate.v4.ST

import scala.io.Source

class PipelineParser(settings: Settings) {

  def parseFromString(string: String): Pipeline = {
    val charStream = CharStreams.fromString(template(string))
    parse(charStream)
  }

  def parseFromFile(file: String): Pipeline = {
    val dslFile = Source.fromFile(file)
    try {
      parseFromString(dslFile.mkString)
    } finally {
      dslFile.close()
    }
  }

  def parseFromInputStream(stream:InputStream):Pipeline ={
    val dslFile = Source.fromInputStream(stream)
    try {
      parseFromString(dslFile.mkString)
    } finally {
      dslFile.close()
    }
  }

  private def parse(charStream: CharStream): Pipeline = {
    val parser = new SqlParser(new CommonTokenStream(new SqlLexer(charStream)))
    val listener = new PipelineListener
    ParseTreeWalker.DEFAULT.walk(listener, parser.sql())
    listener.pipeline
  }


  private def template(template: String) = {
    def templateVariables: Map[String, String] = {
      val variableKey = "stream.template.vars"
      if (settings.config.hasPath(variableKey)) {
        val templateVariables = settings.config.atKey(variableKey)
        import scala.collection.JavaConverters._
        templateVariables.root().keySet().asScala.map(key => key -> templateVariables.getString(key)).toMap
      } else Map()
    }

    def isJobTemplateEnable = {
      settings.config.hasPath(JobTemplateEnable.name) && settings(JobTemplateEnable)
    }

    def templateChar = {
      if (!settings.config.hasPath(JobTemplateStartChar.name) || !settings.config.hasPath(JobTemplateStopChar.name) ){
        ('[',']')
      }else (settings(JobTemplateStartChar), settings(JobTemplateStopChar))
    }

    if (isJobTemplateEnable) {
      val stTemplate = new ST(template, templateChar._1, templateChar._2)
      templateVariables.foreach {
        case (k, v) => stTemplate.add(k, v)
      }
      stTemplate.render()
    } else {
      template
    }
  }

}



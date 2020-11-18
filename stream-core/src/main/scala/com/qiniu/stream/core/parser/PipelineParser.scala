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

import java.io.{InputStream, StringWriter}

import com.qiniu.stream.core.config._
import freemarker.cache.StringTemplateLoader
import freemarker.template.{Configuration, TemplateExceptionHandler}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStream, CharStreams, CommonTokenStream}

import scala.collection.JavaConverters._
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

  def parseFromInputStream(stream: InputStream): Pipeline = {
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
    def dataModels: Map[String, String] = {
      val variableKey = "stream.template.vars"
      if (settings.config.hasPath(variableKey)) {

        settings.config.getConfig(variableKey).root().keySet().asScala.map(key => key ->
          settings.config.getString(variableKey + "." + key)).toMap
      } else Map()
    }

    val cfg = new Configuration(Configuration.VERSION_2_3_23)
    cfg.setDefaultEncoding("UTF-8")
    val stringLoader = new StringTemplateLoader
    stringLoader.putTemplate("pipeline", template)
    cfg.setTemplateLoader(stringLoader)
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER)
    val freeMarkTemplate = cfg.getTemplate("pipeline")
    val writer = new StringWriter()
    freeMarkTemplate.process(dataModels.asJava, writer)
    writer.getBuffer.toString
  }

}



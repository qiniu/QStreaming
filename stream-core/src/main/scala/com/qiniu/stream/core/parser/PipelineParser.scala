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

import com.qiniu.stream.core.config.{JobDSL, JobTemplateEnable, JobTemplateStartChar, JobTemplateStopChar, Pipeline}
import com.qiniu.stream.core.PipelineContext
import org.antlr.v4.runtime.{CharStream, CharStreams, CommonTokenStream}
import org.stringtemplate.v4.ST

import scala.io.Source

class PipelineParser(context: PipelineContext) {

  def parse(): Pipeline = {
    parseFromFile(context.settings(JobDSL))
  }

  def parseFromString(string: String): Pipeline = {
    val charStream = CharStreams.fromString(string)
    parse(charStream)
  }

  def parseFromFile(file: String): Pipeline = {
    val dslFile = Source.fromFile(file)
    try {
      parseFromString(template(dslFile.mkString))
    } finally {
      dslFile.close()
    }
  }

  private def template(template: String) = {
    if (context.settings(JobTemplateEnable)) {
      val (startChar, stopChar) = (context.settings(JobTemplateStartChar), context.settings(JobTemplateStopChar))
      val stTemplate = new ST(template, startChar, stopChar)
      context.settings.templateVariables.foreach {
        case (k, v) => stTemplate.add(k, v)
      }
      stTemplate.render()
    } else {
      template
    }
  }


  def parse(charStream: CharStream): Pipeline = {
    val parser = new SqlParser(new CommonTokenStream(new SqlLexer(charStream)))
    new PipelineVisitor().visit(parser.sql())
  }
}



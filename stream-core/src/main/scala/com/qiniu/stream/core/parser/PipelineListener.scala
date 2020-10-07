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

import com.qiniu.stream.core.config._
import com.qiniu.stream.core.parser.SqlParser.SelectStatementContext
import com.qiniu.stream.util.Logging
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval

class PipelineListener extends SqlBaseListener with Logging {

  val pipeline = new Pipeline

  private def printStatement(context: ParserRuleContext): Unit = {
    val statement = {
      val interval = new Interval(context.start.getStartIndex, context.stop.getStopIndex)
      context.start.getInputStream.getText(interval)
    }
    logDebug(s"parsing statement ${statement}")
  }

  override def enterSqlStatement(ctx: SqlParser.SqlStatementContext): Unit = {
    printStatement(ctx)
    val statement = {
      val interval = new Interval(ctx.start.getStartIndex, ctx.stop.getStopIndex)
      ctx.start.getInputStream.getText(interval)
    }
    pipeline.statements += SqlStatement(statement)
  }


  override def enterCreateSourceTableStatement(ctx: SqlParser.CreateSourceTableStatementContext): Unit = {
    printStatement(ctx)
    pipeline.statements += TableParser.parseSourceTable(ctx)
  }

  override def enterCreateSinkTableStatement(ctx: SqlParser.CreateSinkTableStatementContext): Unit = {
    printStatement(ctx)
    pipeline.statements += TableParser.parseSinkTable(ctx)
  }


  override def enterCreateViewStatement(ctx: SqlParser.CreateViewStatementContext): Unit = {
    printStatement(ctx)
    import scala.collection.convert.wrapAsScala._
    val options = ctx.property().map(ParserHelper.parseProperty).toMap
    val viewType = {
      if (ctx.K_GLOBAL() != null && ctx.K_TEMPORARY() != null) {
        ViewType.globalView
      } else if (ctx.K_TEMPORARY() != null) {
        ViewType.tempView
      } else if (ctx.K_PERSISTED() != null) {
        ViewType.persistedView
      } else {
        ViewType.tempView
      }
    }

    pipeline.statements += CreateViewStatement(ParserHelper.parseSql(ctx.selectStatement()), ctx.tableName().getText, options, viewType)
  }


  override def enterCreateFunctionStatement(ctx: SqlParser.CreateFunctionStatementContext): Unit = {
    printStatement(ctx)
    import scala.collection.JavaConverters._
    val funcBody = {
      val interval = new Interval(ctx.funcBody.start.getStartIndex, ctx.funcBody.stop.getStopIndex)
      ctx.funcBody.start.getInputStream.getText(interval)
    }
    val dataType = if (ctx.functionDataType() != null) {
      val fields = ctx.functionDataType().structField().asScala
      val fieldTypes = fields.map(field => {
        val fieldName = ParserHelper.cleanQuote(field.STRING().getText)
        val fieldType = field.fieldType().getText
        SqlField(fieldName, SqlDataType(fieldType))
      })
      Some(SqlStructType(fieldTypes.toArray))
    } else {
      None
    }
    val funcParams = Option(ctx.funcParam()).map(_.asScala.map(_.getText).mkString(","))
    pipeline.statements += CreateFunctionStatement(dataType, ctx.funcName.getText, funcParams, funcBody)
  }


  def parseSql(selectStatementContext: SelectStatementContext): String = {
    val interval = new Interval(selectStatementContext.start.getStartIndex, selectStatementContext.stop.getStopIndex)
    selectStatementContext.getStart.getInputStream.getText(interval)

  }

  override def enterInsertStatement(ctx: SqlParser.InsertStatementContext): Unit = {
    printStatement(ctx)
    val sql = ParserHelper.parseSql(ctx.selectStatement())
    val tableName = ctx.tableName().getText
    val sinkTableOption = pipeline.sinkTable(tableName)
    sinkTableOption match {
      case Some(sinkTable) => {
        pipeline.statements += InsertStatement(sql, sinkTable)
      }
      case None =>
        pipeline.statements += SqlStatement(s"insert into ${tableName} ${sql}")
    }
  }

}

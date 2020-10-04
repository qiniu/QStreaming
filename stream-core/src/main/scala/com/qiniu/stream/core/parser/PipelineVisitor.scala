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

class PipelineVisitor extends SqlBaseVisitor[Pipeline] with Logging {

  private val pipeline = new Pipeline

  private def printStatement(context: ParserRuleContext): Unit = {
    val statement = {
      val interval = new Interval(context.start.getStartIndex, context.stop.getStopIndex)
      context.start.getInputStream.getText(interval)
    }
    logDebug(s"parsing statement ${statement}")
  }

  override def visitSqlStatement(ctx: SqlParser.SqlStatementContext): Pipeline = {
    printStatement(ctx)
    val statement = {
      val interval = new Interval(ctx.start.getStartIndex, ctx.stop.getStopIndex)
      ctx.start.getInputStream.getText(interval)
    }
    pipeline.statements += SqlStatement(statement)
    pipeline
  }


  override def visitCreateSourceTableStatement(ctx: SqlParser.CreateSourceTableStatementContext): Pipeline = {
    printStatement(ctx)
    pipeline.statements += TableParser.parseSourceTable(ctx)
    pipeline
  }

  override def visitCreateSinkTableStatement(ctx: SqlParser.CreateSinkTableStatementContext): Pipeline = {
    printStatement(ctx)
    pipeline.statements += TableParser.parseSinkTable(ctx)
    pipeline
  }


  override def visitCreateViewStatement(ctx: SqlParser.CreateViewStatementContext): Pipeline = {
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

    val statement = CreateViewStatement(ParserHelper.parseSql(ctx.selectStatement()), ctx.tableName().getText, options, viewType)
    pipeline.statements += statement

    pipeline
  }


  override def visitInsertStatement(ctx: SqlParser.InsertStatementContext): Pipeline = {
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
    pipeline
  }

  override def visitCreateFunctionStatement(ctx: SqlParser.CreateFunctionStatementContext): Pipeline = {
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
    pipeline
  }



  def parseSql(selectStatementContext: SelectStatementContext): String = {
    val interval = new Interval(selectStatementContext.start.getStartIndex, selectStatementContext.stop.getStopIndex)
    selectStatementContext.getStart.getInputStream.getText(interval)

  }

}

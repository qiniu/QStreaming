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
package com.qiniu.stream.spark.core

import com.qiniu.stream.spark.parser.{SqlBaseListener, SqlLexer, SqlParser}
import com.qiniu.stream.spark.config._
import com.qiniu.stream.spark.util.DslUtil
import com.qiniu.stream.util.Logging
import org.antlr.v4.runtime.{CharStream, CommonTokenStream, ParserRuleContext}
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTreeWalker

import scala.collection.mutable.ArrayBuffer


case class Pipeline(jobContent: CharStream, params: Map[String, String] = Map()) extends SqlBaseListener  with Logging {

  val statements: ArrayBuffer[Statement] = new ArrayBuffer[Statement]()

  protected def addStatement(statement: Statement): Unit = {
    statements += statement
  }

  private def parseJob(jobContent: CharStream, params: Map[String, String] = Map()): Unit = {
    val parser = new SqlParser(new CommonTokenStream(new SqlLexer(jobContent)))
    ParseTreeWalker.DEFAULT.walk(this, parser.sql())
  }

  private def printStatement(context: ParserRuleContext): Unit = {
    val statement = {
      val interval = new Interval(context.start.getStartIndex, context.stop.getStopIndex)
      context.start.getInputStream.getText(interval)
    }
    logDebug(s"parsing statement ${statement}")
  }



  def findSinkTable(tableName: String): Option[SinkTable] = statements
    .filter(_.isInstanceOf[SinkTable])
    .map(_.asInstanceOf[SinkTable])
    .find(_.name == tableName)


  /**
   * {@inheritDoc }
   *
   * <p>The default implementation does nothing.</p>
   */
  override def exitSqlStatement(ctx: SqlParser.SqlStatementContext): Unit = {
    printStatement(ctx)
    val statement = {
      val interval = new Interval(ctx.start.getStartIndex, ctx.stop.getStopIndex)
      ctx.start.getInputStream.getText(interval)
    }
    addStatement(SqlStatement(statement))
  }

  /**
   *
   **/
  override def exitCreateSourceTableStatement(ctx: SqlParser.CreateSourceTableStatementContext): Unit = {
    printStatement(ctx)
    addStatement(TableFactory.createSourceTable(ctx))
  }

  override def exitCreateSinkTableStatement(ctx: SqlParser.CreateSinkTableStatementContext): Unit = {
    printStatement(ctx)
    addStatement(TableFactory.createSinkTable(ctx))
  }


  override def exitCreateViewStatement(ctx: SqlParser.CreateViewStatementContext): Unit = {
    printStatement(ctx)
    import scala.collection.convert.wrapAsScala._
    val options = ctx.property().map(DslUtil.parseProperty).toMap
    val viewType = {
      if (ctx.K_GLOBAL() != null && ctx.K_TEMPORARY() != null) {
        ViewType.globalView
      } else if (ctx.K_TEMPORARY() != null) {
        ViewType.tempView
      } else if (ctx.K_PERSISTED() != null) {
        ViewType.persistedView
      } else {
        //为了兼容0.1.0的create view用法，0.1.0都是tempView
        ViewType.tempView
      }
    }

    val statement = CreateViewStatement(DslUtil.parseSql(ctx.selectStatement()), ctx.tableName().getText, options, viewType)
    addStatement(statement)
  }


  override def exitInsertStatement(ctx: SqlParser.InsertStatementContext): Unit = {
    printStatement(ctx)
    val sql = DslUtil.parseSql(ctx.selectStatement())
    val tableName = ctx.tableName().getText
    val sinkTableOption = findSinkTable(tableName)
    sinkTableOption match {
      case Some(sinkTable) => {
        val statement = InsertStatement(sql, sinkTable)
        addStatement(statement)
      }
      case None =>
        addStatement(SqlStatement(s"insert into ${tableName} ${sql}"))
    }
  }

  override def exitCreateFunctionStatement(ctx: SqlParser.CreateFunctionStatementContext): Unit = {
    printStatement(ctx)
    import scala.collection.JavaConverters._
    val funcBody = {
      val interval = new Interval(ctx.funcBody.start.getStartIndex, ctx.funcBody.stop.getStopIndex)
      ctx.funcBody.start.getInputStream.getText(interval)
    }
    val dataType = if (ctx.functionDataType() != null) {
      val fields = ctx.functionDataType().structField().asScala
      val fieldTypes = fields.map(field => {
        val fieldName = DslUtil.cleanQuote(field.STRING().getText)
        val fieldType = field.fieldType().getText
        SqlField(fieldName, SqlDataType(fieldType))
      })
      Some(SqlStructType(fieldTypes.toArray))
    } else {
      None
    }
    val funcParams = Option(ctx.funcParam()).map(_.asScala.map(_.getText).mkString(","))
    addStatement(CreateFunctionStatement(dataType, ctx.funcName.getText, funcParams, funcBody))
  }

  parseJob(jobContent, params)

}

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
package com.qiniu.stream.core.config

import com.qiniu.stream.core.PipelineContext
import com.qiniu.stream.core.config.Statement.{OPT_SHOW_SCHEMA, OPT_SHOW_TABLE}
import com.qiniu.stream.core.config.ViewType.ViewType
import com.qiniu.stream.core.parser.SqlStructType
import com.qiniu.stream.core.translator.{CreateFunctionTranslator, CreateViewTranslator, InsertStatementTranslator, SinkTableTranslator, SourceTableTranslator, SparkSqlTranslator, StatementTranslator}
import com.qiniu.stream.util.Logging


trait Statement extends Logging {

  def debug: Boolean = showSchema || showTable

  protected def translator: StatementTranslator

  def options: Map[String, String]

  def showSchema: Boolean = options.get(OPT_SHOW_SCHEMA).exists(_.toBoolean)

  def showTable: Boolean = options.get(OPT_SHOW_TABLE).exists(_.toBoolean)

  def execute(context: PipelineContext): Unit = {
    if (!context.debug) {
      translator.translate(context)
    }
    context.withDebug(debug)
  }
}

/**
 * original sql statement which supported by spark or flink
 *
 * @param sql
 */
case class SqlStatement(sql: String) extends Statement {
  override def options = Map()

  override protected def translator = SparkSqlTranslator(this)
}

/**
 * any custom dsl statement should extend from this class
 */
trait DSLStatement extends Statement

object ViewType extends Enumeration {
  type ViewType = Value
  val persistedView, globalView, tempView = Value
}

case class CreateViewStatement(sql: String, viewName: String, options: Map[String, String] = Map(), viewType: ViewType = ViewType.tempView) extends DSLStatement {

  override protected def translator: StatementTranslator = CreateViewTranslator(this)
}

case class InsertStatement(sql: String, sinkTable: SinkTable) extends DSLStatement {
  override def options = Map()

  override lazy val debug: Boolean = sinkTable.showSchema || sinkTable.showTable

  override def translator: StatementTranslator = InsertStatementTranslator(this)
}

case class CreateFunctionStatement(dataType: Option[SqlStructType] = None, funcName: String, funcParam: Option[String], funcBody: String) extends DSLStatement {
  override def options = Map()

  override protected def translator: StatementTranslator = CreateFunctionTranslator(this)
}


sealed abstract class Table(props: Map[String, String] = Map()) extends Statement {

  def option(key: String): Option[String] = props.get(key)

  val updateMode: Option[String] = props.get("outputMode").orElse(props.get("saveMode")).orElse(props.get("update-mode")).orElse(props.get("updateMode"))

  override def options: Map[String, String] = props

}


case class SourceTable(streaming: Boolean = true, name: String,
                       connector: Connector,
                       schema: Option[Schema] = None,
                       format: RowFormat,
                       props: Map[String, String] = Map())
  extends Table(props) with Serializable {
  override protected def translator: StatementTranslator = SourceTableTranslator(this)
}


case class BucketSpec(bucket: Int, columns: Seq[String])

case class SinkTable(streaming: Boolean = true, name: String,
                     schema: Option[Schema] = None,
                     format: Option[RowFormat] = None,
                     connectors: Seq[Connector] = Seq(),
                     partitions: Option[Array[String]] = None,
                     bucket: Option[BucketSpec] = None,
                     props: Map[String, String] = Map())
  extends Table(props) with Serializable {

  override protected def translator: StatementTranslator = SinkTableTranslator(this)
}

object Statement {
  val OPT_SHOW_SCHEMA = "showSchema"
  val OPT_SHOW_TABLE = "showTable"
}
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

import com.qiniu.stream.core.parser.SqlParser._
import com.qiniu.stream.core.util.DslUtil.{cleanQuote, parseProperty}
import scala.collection.convert.wrapAsScala._
/**
 * Table Factory create source/sink table from DSL statement
 */
object TableFactory {
  private def createConnector(ctx: ConnectorSpecContext): Connector = {
    Connector(ctx.connectorType.getText, ctx.connectProps.map(parseProperty).toMap)
  }

  private def createFormat(ctx: FormatSpecContext): RowFormat = {
    val props = Option(ctx.property()).map(_.map(parseProperty).toMap).getOrElse(Map())
    RowFormat(ctx.rowFormat().getText, props)
  }

  private def createSchema(ctx: SchemaSpecContext): Schema = {
    val schemaFields = ctx.schemaField().map(field => SchemaField(field.fieldName.getText, field.fieldType.getText))

    val timeField = Option(ctx.timeField()) match {
      case Some(procTimeContext: ProcTimeContext) =>
        Some(ProcTime(procTimeContext.fieldName.getText))
      case Some(rowTimeContext: RowTimeContext) =>
        Some(RowTime(rowTimeContext.fromField.getText, rowTimeContext.eventTime.getText, cleanQuote(rowTimeContext.delayThreadsHold.getText)))
      case _ => None
    }
    Schema(schemaFields, timeField)


  }

  /**
   * create source table from dsl statement
   *
   */
  def createSourceTable(ctx: CreateSourceTableStatementContext): SourceTable = {
    val tableName: String = ctx.tableName().getText
    val connector = createConnector(ctx.connectorSpec())


    val format = Option(ctx.formatSpec()).map(createFormat).getOrElse(RowFormat.json(Map("derive-schemaOption" -> "true")))

    val schemaOption = Option(ctx.schemaSpec()).map(createSchema)

    val isStreamingTable = ctx.K_STREAM() != null

    def createStreamSourceTable: SourceTable = {
      SourceTable(name = tableName, connector = connector, schema = schemaOption, format = format)
    }

    def createBatchSourceTable: SourceTable = {
      SourceTable(streaming = false, tableName, connector, schemaOption, format)
    }

    if (isStreamingTable) createStreamSourceTable else createBatchSourceTable
  }

  /**
   * create sink table from dsl statement
   *
   */
  def createSinkTable(ctx: CreateSinkTableStatementContext): SinkTable = {
    val tableName: String = ctx.tableName().getText
    val tableOptions: Map[String, String] = Option(ctx.tableProperties()).map(_.property().map(parseProperty).toMap).getOrElse(Map())

    val connectors = ctx.connectorSpec().map(createConnector)

    val format = Option(ctx.formatSpec()).map(createFormat)

    val schema = Option(ctx.schemaSpec()).map(createSchema)


    def createStreamSinkTable: SinkTable = {
      val partition = Option(ctx.partitionSpec()).map(_.columns.map(_.getText).toArray)
      SinkTable(streaming = true, tableName, schema, format, connectors, partition, None, tableOptions)
    }

    def createBatchSinkTable: SinkTable = {
      val partition = Option(ctx.partitionSpec()).map(_.columns.map(_.getText).toArray)
      val bucket = Option(ctx.bucketSpec()).map(bck => BucketSpec(bck.bucketNum.getText.toInt, bck.columns.map(_.getText)))
      SinkTable(streaming = false, tableName, schema, format, connectors, partition, bucket, tableOptions)
    }

    val isStreaming = ctx.K_STREAM() != null
    if (isStreaming) createStreamSinkTable else createBatchSinkTable
  }


}

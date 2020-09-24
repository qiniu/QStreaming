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
package com.qiniu.stream.spark.config

import com.qiniu.stream.spark.config.ViewType.ViewType


trait Statement{
  def inDebugMode():Boolean = false
}

/**
 * original sql statement which supported by spark or flink
 * @param sql
 */
case class SqlStatement(sql: String) extends Statement

/**
 * any custom dsl statement should extend from this class
 */
trait DSLStatement extends Statement

object ViewType extends Enumeration {
  type ViewType = Value
  val persistedView, globalView, tempView = Value
}

case class CreateViewStatement(sql: String, viewName: String, options: Map[String, String] = Map(), viewType: ViewType = ViewType.tempView) extends DSLStatement {
  def showSchema = options.get("showSchema") match {
    case Some(value) => value.equalsIgnoreCase("TRUE")
    case _ => false
  }

  def showTable = options.get("showTable") match {
    case Some(value) => value.equalsIgnoreCase("TRUE")
    case _ => false
  }
  override def inDebugMode(): Boolean = showTable || showSchema
}

case class InsertStatement(sql: String, sinkTable: SinkTable) extends DSLStatement {
  override def inDebugMode(): Boolean = sinkTable.showSchema || sinkTable.showTable
}

case class CreateFunctionStatement(dataType: Option[SqlStructType] = None, funcName: String, funcParam: Option[String], funcBody: String) extends DSLStatement


sealed abstract class Table(streaming: Boolean,
                            input: Boolean,
                            name: String,
                            connector: Option[Connector] = None,
                            schema: Option[Schema] = None,
                            format: Option[RowFormat] = None,
                            props: Map[String, String] = Map()) extends Statement {

  def option(key: String): Option[String] = props.get(key)

  val updateMode: Option[String] = props.get("outputMode").orElse(props.get("saveMode")).orElse(props.get("update-mode")).orElse(props.get("updateMode"))


  def showSchema: Boolean = option("showSchema") match {
    case Some(value )if value.equalsIgnoreCase("true")=> true
    case _=> false
  }

  def showTable: Boolean = option("showTable") match {
    case Some(value )if value.equalsIgnoreCase("true")=> true
    case _=> false
  }

  override def inDebugMode(): Boolean = showTable || showSchema
}


case class SourceTable(streaming: Boolean = true, name: String,
                       connector: Connector,
                       schema: Option[Schema]=None,
                       format: RowFormat,
                       props: Map[String, String] = Map())
  extends Table(streaming, true, name, Some(connector), schema, Some(format), Map()) with Serializable


case class BucketSpec(bucket: Int, columns: Seq[String])

case class SinkTable(streaming: Boolean = true, name: String,
                     schema: Option[Schema] = None,
                     format: Option[RowFormat] = None,
                     connectors: Seq[Connector] = Seq(),
                     partitions: Option[Array[String]] = None,
                     bucket: Option[BucketSpec] = None,
                     props: Map[String, String] = Map())
  extends Table(streaming, false, name, None, schema, format, props)  with Serializable{


}

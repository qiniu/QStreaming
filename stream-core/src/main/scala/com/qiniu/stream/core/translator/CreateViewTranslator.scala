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
package com.qiniu.stream.core.translator

import com.qiniu.stream.core.{PipelineContext, config}
import com.qiniu.stream.core.config.{CreateViewStatement, RowTime, ViewType}
import com.qiniu.stream.core.source.WaterMarker
import com.qiniu.stream.core.util.DatasetUtils
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

case class CreateViewTranslator(statement: CreateViewStatement) extends StatementTranslator with WaterMarker with Logging {
  val waterMark: Option[RowTime] = statement.options.get("waterMark").map(_.split(",\\s*")) match {
    case Some(Array(fromField, eventTime, delay)) => Some(config.RowTime(fromField, eventTime, delay))
    case Some(Array(eventTime, delay)) => Some(config.RowTime(eventTime, eventTime, delay))
    case _ => None
  }


  def translate(context: PipelineContext): Unit = {
    val sparkSession = context.sparkSession
    log.debug(s"parsing query: \n${statement.sql}")
    statement.viewType match {
      case ViewType.`tempView` => {
        var table = sparkSession.sql(statement.sql)
        table = repartition(table)
        table = withWaterMark(table, waterMark)
        table.createOrReplaceTempView(statement.viewName)
      }
      case ViewType.`globalView` => {
        var table = sparkSession.sql(statement.sql)
        table = repartition(table)
        table = withWaterMark(table, waterMark)
        table.createOrReplaceGlobalTempView(statement.viewName)
      }
      case ViewType.`persistedView` => {
        sparkSession.sql(s"create view ${statement.viewName} as ${statement.sql}")
      }
    }
    //for debug purpose
    if (statement.showSchema) {
      sparkSession.table(statement.viewName).printSchema()
    }
    //for debug purpose
    if (statement.showTable) {
      val table = sparkSession.table(statement.viewName)
      DatasetUtils.showTable(table)
    }
  }


  private def repartition(table: DataFrame): DataFrame = {
    var dataFrame = statement.options.get("repartition") match {
      case Some(repartition) =>
        val partitionColumns = repartition.split(",\\s*")
        partitionColumns match {
          case Array(num) if num.forall(_.isDigit) =>
            log.debug("Repartitioned with size. repartition={}", repartition)
            table.repartition(num.toInt)
          case Array(num, tails@_*) if num.forall(_.isDigit) =>
            log.debug("Repartitioned with fix size and columns. repartition={}", repartition)
            table.repartition(num.toInt, tails.map(table.col): _*)
          case Array() =>
            log.debug("Repartition option with no effect. repartition={}", repartition)
            table
          case allColumns =>
            log.debug("Repartitioned with columns. repartition={}", repartition)
            table.repartition(allColumns.map(table.col): _*)
        }
      case None =>
        log.debug("No repartition")
        table
    }

    val coalesce: Option[Int] = statement.options.get("coalesce").map(_.toInt)
    dataFrame = coalesce.map(dataFrame.coalesce).getOrElse(dataFrame)

    dataFrame
  }
}

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
package org.apache.spark.sql.execution.streaming.phoenix

import java.sql.DriverManager
import org.apache.phoenix.spark._

import com.qiniu.stream.util.Logging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.phoenix.util.SchemaUtil
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.execution.streaming.phoenix.phoenix.WrapPhoenixColumnInfo
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

//Copy from Phoenix RDD
class PhoenixSink(parameters:Map[String,String], outputMode :OutputMode) extends Sink with Logging {

  private val (tableName,zkUrl):(String,String) = {
    require(parameters.contains("table") && parameters.contains("zkUrl"),
      s"phoenix sink must contains table and zkUrl parameters: ${parameters.mkString(",")}")

    require(!(parameters.contains("include-columns") && parameters.contains("exclude-columns")),
      s"phoenix sink can not defined include-columns and exclude-columns parameters at the same time")

    ( parameters("table"),
      parameters("zkUrl"))
  }

  require(outputMode == OutputMode.Update(),
    s"phoenix sink only support update mode: $outputMode")

  log.info(s"phoenix sink parameters: ${parameters.mkString(",")}")

  @volatile var checkedSchema = false
  private val tableSchema:Map[String,DataType]= {
    Try {
      Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")
      DriverManager.getConnection(s"jdbc:phoenix:$zkUrl")
    } match {
      case Success(c) ⇒
        try {
          SchemaUtil.generateColumnInfo(c, tableName.toUpperCase, null, true).map(column ⇒ {
            (parseColumn(column.getColumnName), column.toDataType)
          }).toMap
        } finally {
          log.info("close phoenix connection")
          c.close()
        }
      case Failure(e) ⇒
        throw e
    }
  }

  //"0"."timestamp" -> timestamp 对字段进行规整化
  private def parseColumn(name:String):String={
    name.split("\\.").last.drop(1).dropRight(1)
  }

  private def checkSchema(sinkSchema:Map[String,DataType]):Unit={
    log.info(s"phoenix table: $tableName schema: ${tableSchema.mkString(",")}")
    log.info(s"sink table schema: ${sinkSchema.mkString(",")}")
    //todo check more schema rule
    //check the sink table field is long and if the corresponding field of the phoenix table is an integer, an error is reported.
    sinkSchema.foreach({
      case (name,LongType)⇒
        tableSchema.get(name.toUpperCase) match {
          case Some(IntegerType) ⇒
            throw new RuntimeException(s"column: $name sink table type: $LongType phoenix table type: $IntegerType")
          case _ ⇒
        }
      case _  ⇒
    })

  }
  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    val schema = data.schema
    if(!checkedSchema) {
      checkSchema(schema.fields.map(field⇒(field.name,field.dataType)).toMap)
      checkedSchema = true
    }
    val res = data.queryExecution.toRdd.mapPartitions { rows =>
      val converter = CatalystTypeConverters.createToScalaConverter(schema)
      rows.map(converter(_).asInstanceOf[Row])
    }

    val df =  data.sparkSession.createDataFrame(res, schema)
    val config = HBaseConfiguration.create()
    parameters.foreach{
      case (k,v)=> config.set(k, v)
    }
    printConfig(config)
    df.saveToPhoenix(tableName, config, Some(zkUrl))
  }

  private def printConfig(config: Configuration) = {
    log.info("createRelation properties")

    val itr = config.iterator()

    while (itr.hasNext) {
      val entry = itr.next();
      log.info(s"${entry.getKey}=${entry.getValue}")
    }
  }
}

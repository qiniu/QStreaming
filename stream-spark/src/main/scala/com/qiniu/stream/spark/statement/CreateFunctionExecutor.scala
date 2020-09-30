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
package com.qiniu.stream.spark.statement

import com.qiniu.stream.spark.config.{BooleanDataType, ByteDataType, CreateFunctionStatement, DateDataType, DoubleDataType, FloatDataType, IntDataType, LongDataType, ShortDataType, SmallIntDataType, SqlStructType, StringDataType, TimeStampDataType, TinyIntDataType}
import com.qiniu.stream.spark.core.PipelineContext
import com.qiniu.stream.spark.source.WaterMarker
import com.qiniu.stream.spark.udf.ScalaDynamicUDF
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.FunctionIdentifier
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}

import scala.util.Try

case class CreateFunctionExecutor(udfStatement: CreateFunctionStatement) extends StatementExecutor with WaterMarker with Logging {
  override def execute(jobContext :PipelineContext, sparkSession: SparkSession): Unit = {

    val params = udfStatement.funcParam match {
      case Some(params) => params
      case None => ""
    }
    val func = s"def apply (${params}) = ${udfStatement.funcBody}"
    log.debug(s"parsing udf statement: \n${func}")
    val (fun, argumentTypes, returnType) = udfStatement.dataType match  {
      case Some(dataType)=>{
        ScalaDynamicUDF(func,toDataType(dataType))
      }
      case None=> ScalaDynamicUDF(func)
    }
    val inputTypes: Seq[DataType] = Try(argumentTypes.toSeq).getOrElse(Nil)

    def builder(e: Seq[Expression]) = ScalaUDF(fun, returnType, e, Nil, inputTypes, Some(udfStatement.funcName))

    sparkSession.sessionState.functionRegistry.registerFunction(new FunctionIdentifier(udfStatement.funcName), builder)

  }

  private def toDataType(sqlDataType:SqlStructType): DataType ={
    val fields = sqlDataType.fields.map(field=>{
      val dataType = field.dataType match {
        case _:ShortDataType => DataTypes.ShortType
        case _:IntDataType=> DataTypes.IntegerType
        case _:SmallIntDataType=>DataTypes.IntegerType
        case _:TinyIntDataType=>DataTypes.ShortType
        case _:LongDataType=>DataTypes.LongType
        case _:StringDataType=>DataTypes.StringType
        case _:BooleanDataType=>DataTypes.BooleanType
        case _:DateDataType=>DataTypes.DateType
        case _:TimeStampDataType=>DataTypes.TimestampType
        case _:ByteDataType=>DataTypes.ByteType
        case _:FloatDataType=>DataTypes.FloatType
        case _:DoubleDataType=>DataTypes.DoubleType
        case _=>DataTypes.StringType
      }
      DataTypes.createStructField(field.name,dataType,true)
    })
    DataTypes.createStructType(fields)
  }
}

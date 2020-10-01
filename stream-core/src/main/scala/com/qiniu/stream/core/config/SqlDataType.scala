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


trait SqlDataType

object SqlDataType{
  def apply(value:String):SqlDataType ={

    value match {
      case "INTEGER"=> IntDataType()
      case "STRING"=>StringDataType()
      case "BOOLEAN"=>BooleanDataType()
      case "LONG"=>LongDataType()
      case "TINYINT"=>TinyIntDataType()
      case "SMALLINT"=>SmallIntDataType()
      case "TIMESTAMP"=>TimeStampDataType()
      case "DATE"=> DateDataType()
      case "TIME"=>TimeDataType()
      case "DATETIME"=>DateTimeDataType()
      case "DOUBLE"=>DoubleDataType()
      case "FLOAT"=>FloatDataType()
      case "SHORT"=>ShortDataType()
      case "BYTE"=>ByteDataType()
      case "VARCHAR"=>VarcharDataType()
      case _=>StringDataType()
    }
  }
}

case class IntDataType() extends  SqlDataType
case class StringDataType() extends  SqlDataType
case class BooleanDataType() extends  SqlDataType
case class LongDataType() extends  SqlDataType
case class TinyIntDataType() extends  SqlDataType
case class SmallIntDataType() extends  SqlDataType
case class TimeStampDataType() extends  SqlDataType
case class DateDataType() extends  SqlDataType
case class TimeDataType() extends  SqlDataType
case class DateTimeDataType() extends  SqlDataType
case class DoubleDataType() extends  SqlDataType
case class FloatDataType() extends  SqlDataType
case class ShortDataType() extends  SqlDataType
case class ByteDataType() extends  SqlDataType
case class VarcharDataType() extends  SqlDataType

case class SqlField(name:String,dataType: SqlDataType)

case class SqlStructType(fields:Array[SqlField]) extends SqlDataType
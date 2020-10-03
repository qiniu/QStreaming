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

import org.apache.spark.sql.types.StructType

sealed trait TimeField

case class ProcTime(fieldName: String) extends TimeField

case class RowTime(fromField: String, eventTime: String, delayThreadsHold: String) extends TimeField

case class SchemaField(fieldName: String, fieldType: String)

case class Schema(fields: Seq[SchemaField], timeField: Option[TimeField] = None)


object Schema {

  implicit class RichSchema(schema: Schema) {
    def structType: StructType = {
      StructType.fromDDL(toDDL)
    }

    def toDDL: String = {
      schema.fields.map(field => s"${field.fieldName} ${field.fieldType}").mkString(",")
    }
  }

}

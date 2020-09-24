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

case class RowFormat(name: String, props: Map[String, String] = Map()) {

  def isCsvFormat: Boolean = "CSV".equals(name.toUpperCase)

  def isAvroFormat: Boolean = "AVRO".equals(name.toUpperCase)

  def isRegExFormat: Boolean = "REGEX".equals(name.toUpperCase)

  def isJsonFormat: Boolean = "JSON".equals(name.toUpperCase)

  def isTextFormat: Boolean = "TEXT".equals(name.toUpperCase)

  val rawSchema: Option[String] = props.get("schema")

  def schema: Option[Schema] = rawSchema.map(_.split(",\\s*").map(_.split("\\s+")).filter(_.length == 2).map {
    case Array(fieldName, fieldType) => SchemaField(fieldName, fieldType)
  }).map(Schema(_))

}

object RowFormat {

  def csv(props: Map[String, String] = Map()) = RowFormat("CSV", props)

  def json(props: Map[String, String] = Map()) = RowFormat("JSON", props)

  def avro(props: Map[String, String] = Map()) = RowFormat("AVRO", props)

  def text(props: Map[String, String] = Map()) = RowFormat("TEXT", props)
}



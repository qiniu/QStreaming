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
package com.qiniu.stream.spark.config

case class Connector(name: String, options: Map[String, String] = Map()) {

  def isKafka: Boolean = name.toLowerCase == "kafka"

  def isConsole: Boolean = name.toLowerCase == "console"

  def isMemory: Boolean = name.toLowerCase() == "memory"

  def isHBase: Boolean = name.toLowerCase() == "hbase"

  def isCustom: Boolean = name.toLowerCase() == "custom"

  def option(key: String): Option[String] = options.get(key)

  def conditionExpr: Option[String] = option("where")

  def reader: Option[String] = option("reader")

  def includeColumns: Option[Array[String]] = option("selectExpr").orElse(option("include-columns")).map(_.split(",\\s*"))

  def excludeColumns: Option[Array[String]] = option("dropColumns").orElse(option("exclude-columns")).map(_.split(",\\s*"))

  def outputMode: Option[String] = option("outputMode").orElse(option("update-mode")).orElse(option("saveMode"))

  lazy val partitions: Option[Array[String]] = options.get("partitions").map(_.split(",\\s*"))

  lazy val buckets: Option[BucketSpec] = options.get("buckets").map(_.split("|\\s*")) match {
    case Some(Array(bucket, bucketColumns)) => Some(BucketSpec(bucket.toInt, bucketColumns.split(",\\s*")))
    case None => None

  }

}

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
package com.qiniu.stream.core.source

import com.qiniu.stream.core.config.SourceTable
import com.qiniu.stream.core.source.file.FileStreamReader
import com.qiniu.stream.core.source.kafka.KafkaStreamReader
import com.qiniu.stream.core.source.redis.RedisStreamReader
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

class StreamReader extends Reader with WaterMarker with Logging {

  lazy val streamReaders = Map(
    "kafka"->new KafkaStreamReader,
    "file"-> new FileStreamReader,
    "redis"-> new RedisStreamReader
  )

  override def read(sparkSession: SparkSession, sourceTable: SourceTable): DataFrame = {

    streamReaders.get(sourceTable.connector.name) match {
      case Some(reader)=> reader.read(sparkSession,sourceTable)
      case None=> throw new UnsupportedOperationException(s"${sourceTable.connector.name} is not supported")
    }
  }
}
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

import com.qiniu.stream.core.PipelineContext
import com.qiniu.stream.core.config.SourceTable
import com.qiniu.stream.core.source.{BatchReader, Reader, StreamReader}
import com.qiniu.stream.core.util.DatasetUtils

case class SourceTableTranslator(table:SourceTable) extends StatementTranslator {
  override def translate(context: PipelineContext): Unit = {
    val reader = if (table.connector.isCustom) {
      require(table.connector.reader.isDefined,"reader is required when using custom format")
      Class.forName(table.connector.reader.get).newInstance().asInstanceOf[Reader]
    } else {
      if (!table.streaming)
        new BatchReader
      else {
        new StreamReader
      }
    }
    val dataFrame = reader.read(context.sparkSession, table)
    dataFrame.createOrReplaceTempView(table.name)
    if (table.showSchema) {
      dataFrame.printSchema()
    }
    if (table.showTable ) {
      DatasetUtils.showTable(dataFrame)
    }
    context.withDebug(table.debug)
  }
}

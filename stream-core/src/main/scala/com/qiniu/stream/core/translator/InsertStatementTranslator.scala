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
import com.qiniu.stream.core.config.{InsertStatement, SinkTable}
import com.qiniu.stream.core.sink.{BatchWriter, StreamWriter}
import com.qiniu.stream.core.util.DatasetUtils
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

case class InsertStatementTranslator(insertStatement: InsertStatement) extends StatementTranslator with Logging {

  override def translate(context: PipelineContext): Unit = {
    log.debug(s"parsing insert statement:\n ${insertStatement.sql}")
    val dataFrame = context.sparkSession.sql(insertStatement.sql)
    write(insertStatement.sinkTable, dataFrame)
  }

  private def write(table: SinkTable, dataFrame: DataFrame) = {
    val writer = if (table.streaming)
      new StreamWriter
    else
      new BatchWriter
    writer.write(dataFrame, table)

  }

}

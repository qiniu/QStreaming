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
package org.apache.spark.sql.execution.streaming.hbase


import org.apache.spark.sql.sources._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

class DefaultSource extends RelationProvider with CreatableRelationProvider {

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation =
    HBaseRelation(parameters, None)(sqlContext)


  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    val relation = HBaseInsertRelation(data, parameters)(sqlContext)
    relation.insert(data, mode == SaveMode.Overwrite)
    relation
  }
}

case class HBaseInsertRelation(
                                dataFrame: DataFrame,
                                parameters: Map[String, String]
                              )(@transient val sqlContext: SQLContext)
  extends BaseRelation with InsertableRelation {

  override def insert(data: DataFrame, overwrite: Boolean): Unit = {
    val repository = HBaseRepository(parameters)
    repository.writer(dataFrame, overwrite)
  }

  override def schema: StructType = dataFrame.schema
}

case class HBaseRelation(
                          parameters: Map[String, String],
                          userSpecifiedschema: Option[StructType]
                        )(@transient val sqlContext: SQLContext)
  extends BaseRelation {

  override def schema: StructType = {
    import org.apache.spark.sql.types._
    StructType(
      Array(
        StructField("rowkey", StringType, false),
        StructField("content", StringType)
      )
    )

  }
}
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
package com.qiniu.stream.spark.core

import com.qiniu.stream.util.Logging
import com.typesafe.config.Config
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSessionFactory extends Logging {

  def load(config: Config) = {
    val sparkConf: SparkConf = {
      val sparkConf = new SparkConf()
      if (config.hasPath("spark")){
        import scala.collection.JavaConversions._
        config.getConfig("spark").entrySet().foreach(e => {
          sparkConf.set(e.getKey, config.getString("spark." + e.getKey))
        })
      }
      sparkConf
    }
    SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()


  }


}

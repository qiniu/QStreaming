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
package com.qiniu.stream.core.example

import com.qiniu.stream.core.PipelineRunner
import com.typesafe.config.ConfigFactory
import net.manub.embeddedkafka.EmbeddedKafka.{publishStringMessageToKafka, withRunningKafkaOnFoundPort}
import net.manub.embeddedkafka.EmbeddedKafkaConfig
import org.apache.spark.sql.SparkSession

object KafkaToConsoleExample extends App {

  val config = ConfigFactory.load()

  val spark = SparkSession.builder().master("local[1]").getOrCreate()

  val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)



  withRunningKafkaOnFoundPort(userDefinedConfig) { implicit actualConfig =>

    val pipelineDsl =
      s"""
          create stream input table user_behavior(
         |  user_id LONG,
         |  item_id LONG,
         |  category_id LONG,
         |  behavior STRING,
         |  ts TIMESTAMP,
         |  eventTime as ROWTIME(ts,'2 seconds')
         |) using kafka(
         |  kafka.bootstrap.servers="localhost:${actualConfig.kafkaPort}",
         |  startingOffsets=earliest,
         |  subscribe="user_behavior",
         |  "group-id"="user_behavior"
         |);
         |
         |create stream output table behavior_cnt_per_hour using console TBLPROPERTIES("update-mode"=update);
         |
         |create view v_behavior_cnt_per_hour as
         |SELECT
         |   window(eventTime, "1 seconds") as window,
         |   COUNT(*) as behavior_cnt,
         |   behavior
         |FROM user_behavior
         |GROUP BY
         |  window(eventTime, "1 seconds"),
         |  behavior;
         |
         |insert into behavior_cnt_per_hour
         |select  cast(window.start as long)  as time, behavior_cnt, behavior
         |from v_behavior_cnt_per_hour;
        """.stripMargin

    val time = System.currentTimeMillis()
    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"buy",
         |	"ts":${time}
         |}""".stripMargin)


    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"buy",
         |	"ts":${(time)}
         |}""".stripMargin)

    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"order",
         |	"ts":${(time+20)}
         |}""".stripMargin)

    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"order",
         |	"ts":${(time+1000)}
         |}""".stripMargin)

    val pipelineRunner = PipelineRunner(config, spark, pipelineDsl)
    pipelineRunner.start()

  }


}

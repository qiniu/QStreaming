package com.qiniu.stream.spark.example

import com.qiniu.stream.spark.core.JobOperator
import com.typesafe.config.ConfigFactory
import net.manub.embeddedkafka.EmbeddedKafka.{publishStringMessageToKafka, withRunningKafkaOnFoundPort}
import net.manub.embeddedkafka.EmbeddedKafkaConfig
import org.apache.spark.sql.SparkSession

object KafkaToKafkaExample extends App {

  val config = ConfigFactory.load()

  val spark = SparkSession.builder().master("local[1]").getOrCreate()

  val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)



  withRunningKafkaOnFoundPort(userDefinedConfig) { implicit actualConfig =>

    val jobContent =
      s"""
          create stream input table user_behavior(
         |  user_id LONG,
         |  item_id LONG,
         |  category_id LONG,
         |  behavior STRING,
         |  ts TIMESTAMP,
         |  eventTime as ROWTIME(ts,'1 minutes')
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
         |   window(eventTime, "1 minutes") as window,
         |   COUNT(*) as behavior_cnt,
         |   behavior
         |FROM user_behavior
         |GROUP BY
         |  window(eventTime, "1 minutes"),
         |  behavior;
         |
         |insert into behavior_cnt_per_hour
         |select  from_unixtime(cast(window.start as LONG)/1000,'yyyy-MM-dd HH:mm:ss') as time, behavior_cnt, behavior
         |from v_behavior_cnt_per_hour;
        """.stripMargin

    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"buy",
         |	"ts":${System.currentTimeMillis()}
         |}""".stripMargin)

    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"buy",
         |	"ts":${System.currentTimeMillis()+1000}
         |}""".stripMargin)

    publishStringMessageToKafka("user_behavior",
      s"""{
         |	"user_id":1001,
         |	"item_id":2001,
         |	"category_id":1,
         |	"behavior":"buy",
         |	"ts":${System.currentTimeMillis()+60000}
         |}""".stripMargin)
    val jobOperator = JobOperator(config, spark, jobContent)

    jobOperator.start()

  }


}

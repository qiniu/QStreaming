package com.qiniu.stream.core.parser

import com.qiniu.stream.core.config.{CreateViewStatement, InsertStatement, Settings, SinkTable, SourceTable}
import org.apache.spark.sql.SparkSession
import org.scalatest.funsuite.AnyFunSuite

class PipelineParserTest extends AnyFunSuite{

  val sparkSession = SparkSession.builder().master("local").getOrCreate()
  val jobContent =
    """  create stream input table user_behavior(
      |  user_id LONG,
      |  item_id LONG,
      |  category_id LONG,
      |  behavior STRING
      |) using kafka(
      |  kafka.bootstrap.servers="kafka:9092",
      |  startingOffsets="earliest",
      |  subscribe="user_behavior",
      |  "group-id"="user_behavior"
      |);
      |
      |create stream output table behavior_cnt_per_hour using kafka(
      |   kafka.bootstrap.servers="kafka:9092",
      |   topic="behavior_cnt_per_hour"
      | ) TBLPROPERTIES(outputMode="update", checkpointLocation="/tmp/checkpoint/behavior_cnt_per_hour");
      |
      |
      |create view v_user_behavior with (waterMark="proc_time, 1 seconds") as
      |select
      |  unix_timestamp() as proc_time,
      |  user_id,
      |  item_id,
      |  category_id,
      |  behavior
      |from user_behavior a;
      |
      |
      |create view   v_behavior_cnt_per_hour as
      |SELECT
      |   window(proc_time, "1 seconds").start as proc_time,
      |   COUNT(*) as count,
      |   behavior
      |FROM v_user_behavior
      |GROUP BY
      |  window(proc_time, "1 seconds"),
      |  behavior;
      |
      |insert into  behavior_cnt_per_hour
      |SELECT
      |  to_json(struct(count,behavior)) value
      |from  v_behavior_cnt_per_hour;""".stripMargin

  test("Parse dsl from string"){
    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(jobContent)
    assert(pipeline.statements.size==5)
  }

  test("Parse dsl and verify statement"){
    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(jobContent)
    assert(pipeline.statements.size==5)
    assert(pipeline.statements(0).isInstanceOf[SourceTable])
    assert(pipeline.statements(1).isInstanceOf[SinkTable])
    assert(pipeline.statements(2).isInstanceOf[CreateViewStatement])
    assert(pipeline.statements(3).isInstanceOf[CreateViewStatement])
    assert(pipeline.statements(4).isInstanceOf[InsertStatement])
  }
}

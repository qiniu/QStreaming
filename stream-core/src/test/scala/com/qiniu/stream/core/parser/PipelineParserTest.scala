package com.qiniu.stream.core.parser

import com.amazon.deequ.checks.CheckLevel
import com.qiniu.stream.core.config.{CreateFunctionStatement, CreateViewStatement, InsertStatement, RowFormat, Settings, SinkTable, SourceTable, VerifyStatement, ViewType}
import org.scalatest.funsuite.AnyFunSuite

class PipelineParserTest extends AnyFunSuite{
  val createSourceTableStatement =
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
      |);""".stripMargin

  val createSinkTableStatement =
    """
      |create stream output table behavior_cnt_per_hour using kafka(
      |   kafka.bootstrap.servers="kafka:9092",
      |   topic="behavior_cnt_per_hour"
      | ) TBLPROPERTIES(outputMode="update", checkpointLocation="/tmp/checkpoint/behavior_cnt_per_hour");
       """.stripMargin


  val createViewStatement = s"""create view v_user_behavior with (waterMark="proc_time, 1 seconds") as
                      |  select
                      |  unix_timestamp() as proc_time,
                      |  user_id,
                      |  item_id,
                      |  category_id,
                      |  behavior
                      |from user_behavior a;""".stripMargin

  val insertIntoStatement =
    s"""
       |insert into  behavior_cnt_per_hour
       |SELECT
       |  to_json(struct(count,behavior)) value
       |from  v_behavior_cnt_per_hour;""".stripMargin

  val createFunctionStatement =
    s"""  create function percentRatio(edgeCount: Long, all5xxCount: Long, source5xxCount: Long) = {
       | if (all5xxCount == 0) {
       |       "0"
       |     } else {
       |       val ratio = java.math.BigDecimal.valueOf((all5xxCount - source5xxCount) * 1.0 / edgeCount)
       |           .setScale(4, java.math.BigDecimal.ROUND_HALF_UP)
       |       if (ratio.doubleValue() == 0) "0"
       |       else ratio.toPlainString
       |     }
       |};""".stripMargin

  val createTestStatement =
    s"""  CREATE TEST testName(testLevel="Error",testOutput="testResult") on dataset WITH
       |   numRows()=5 and
       |   isNotNull(id) and
       |   isUnique(id) and
       |   isNotNull(productName) and
       |   isContainedIn(priority, ["high", "low"]) and
       |   isNonNegative(numViews)  and
       |   containsUrl(description) >= 0.5 and
       |   hasApproxQuantile(numViews, 0.5) <= 10;""".stripMargin

  test("parse createSourceTableStatement"){

    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createSourceTableStatement)
    val sourceTable = pipeline.statements.find(_.isInstanceOf[SourceTable]).map(_.asInstanceOf[SourceTable])
    assert(sourceTable.isDefined)
    sourceTable.foreach(it =>{
      assert(it.name == "user_behavior")
      assert(it.streaming)
      assert(it.format.isJsonFormat)
      assert(it.schema.isDefined)
      assert(it.schema.get.fields.size==4)
      assert(it.connector.name=="kafka")
      assert(it.connector.options.size==4)
      assert(it.props.isEmpty)
    })

  }

  test("Parse createSinkTableStatement"){
    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createSinkTableStatement)
    val sinkTable = pipeline.statements.find(_.isInstanceOf[SinkTable]).map(_.asInstanceOf[SinkTable])
    assert(sinkTable.isDefined)
    sinkTable.foreach(it =>{
      assert(it.name == "behavior_cnt_per_hour")
      assert(it.streaming)
      assert(it.schema.isEmpty)
      assert(it.connectors.nonEmpty)
      val kafkaConnector = it.connectors.find(_.name=="kafka")
      kafkaConnector foreach { it=>
        assert(it.options.size==2)
      }
      assert(it.props.size==2)
    })
  }

  test("Parse createViewStatement"){

    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createViewStatement)
    val createViewStmt = pipeline.statements.find(_.isInstanceOf[CreateViewStatement]).map(_.asInstanceOf[CreateViewStatement])
    assert(createViewStmt.isDefined)
    createViewStmt.foreach{
      it=>
        assert(it.viewName=="v_user_behavior")
        assert(it.options.size==1)
        assert(it.viewType== ViewType.tempView)
    }
  }

  test("Parse InsertIntoStatement"){

    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createSinkTableStatement + insertIntoStatement)
    val insertStatement = pipeline.statements.find(_.isInstanceOf[InsertStatement]).map(_.asInstanceOf[InsertStatement])
    assert(insertStatement.isDefined)
    insertStatement.foreach{
      it=>
        assert(it.sinkTable!=null)
        assert(it.sinkTable.name == "behavior_cnt_per_hour")
    }
  }

  test("Parse createFunctionStatement"){


    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createFunctionStatement)
    val createFunStatement = pipeline.statements.find(_.isInstanceOf[CreateFunctionStatement]).map(_.asInstanceOf[CreateFunctionStatement])
    assert(createFunStatement.isDefined)
    createFunStatement foreach{
      it=>
        assert(it.funcName=="percentRatio")
        assert(it.dataType.isEmpty)
        assert(it.funcParam.isDefined)
    }
  }


  test("parse createTestStatement"){

    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(createTestStatement)

    val verifyStatement = pipeline.statements.find(_.isInstanceOf[VerifyStatement]).map(_.asInstanceOf[VerifyStatement])
    assert(verifyStatement.isDefined)

    verifyStatement foreach{
      it=>
        assert(it.name=="testName")
        assert(it.input =="dataset")
        assert(it.check != null)
        assert(it.check.level==CheckLevel.Error)
    }
  }

  test("parse pipeline"){
    val jobContent =
      s""" ${createSourceTableStatement}
         | $createSinkTableStatement
         | $createViewStatement
         | $createFunctionStatement
         | $createTestStatement
         | $insertIntoStatement """.stripMargin
    val settings = Settings.load()
    val pipeline = new PipelineParser(settings).parseFromString(jobContent)

    assert(pipeline.statements.size==6)

    assert(pipeline.statements(0).isInstanceOf[SourceTable])
    assert(pipeline.statements(1).isInstanceOf[SinkTable])
    assert(pipeline.statements(2).isInstanceOf[CreateViewStatement])
    assert(pipeline.statements(3).isInstanceOf[CreateFunctionStatement])
    assert(pipeline.statements(4).isInstanceOf[VerifyStatement])
    assert(pipeline.statements(5).isInstanceOf[InsertStatement])

  }
}

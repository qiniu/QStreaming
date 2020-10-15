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
package com.qiniu.stream.core.config

import com.amazon.deequ.checks.{Check, CheckLevel}
import com.amazon.deequ.constraints.{ConstrainableDataTypes, Constraint}
import com.amazon.deequ.constraints.Constraint.{approxCountDistinctConstraint, approxQuantileConstraint, completenessConstraint, complianceConstraint, dataTypeConstraint, maxConstraint, maxLengthConstraint, meanConstraint, minConstraint, minLengthConstraint, patternMatchConstraint, sizeConstraint, sumConstraint, uniquenessConstraint}
import com.qiniu.stream.core.PipelineContext
import com.qiniu.stream.core.config.ViewType.ViewType
import com.qiniu.stream.core.exceptions.ParsingException
import com.qiniu.stream.core.parser.SqlStructType
import com.qiniu.stream.core.translator._
import com.qiniu.stream.util.Logging

import scala.collection.mutable.ArrayBuffer


trait Statement extends Logging {
  def execute(context: PipelineContext)
}

class Pipeline extends Statement {

  var statements: ArrayBuffer[Statement] = ArrayBuffer()

  def sinkTable(tableName: String): Option[SinkTable] = statements.filter(_.isInstanceOf[SinkTable]).map(_.asInstanceOf[SinkTable]).find(_.name == tableName)

  override def execute(context: PipelineContext): Unit = PipelineTranslator(this).translate(context)
}

/**
 * original sql statement which supported by spark or flink
 *
 * @param sql
 */
case class SqlStatement(sql: String) extends Statement {
  override def execute(context: PipelineContext): Unit = SparkSqlTranslator(this).translate(context)
}

/**
 * any custom dsl statement should extend from this class
 */
trait DSLStatement extends Statement

object ViewType extends Enumeration {
  type ViewType = Value
  val globalView, tempView = Value
}

case class CreateViewStatement(sql: String, viewName: String, options: Map[String, String] = Map(), viewType: ViewType = ViewType.tempView) extends DSLStatement {

  override def execute(context: PipelineContext): Unit = CreateViewTranslator(this).translate(context)
}

case class InsertStatement(sql: String, sinkTable: SinkTable) extends DSLStatement {

  override def execute(context: PipelineContext): Unit = InsertStatementTranslator(this).translate(context)
}

case class CreateFunctionStatement(dataType: Option[SqlStructType] = None, funcName: String, funcParam: Option[String], funcBody: String) extends DSLStatement {
  override def execute(context: PipelineContext): Unit = CreateFunctionTranslator(this).translate(context)
}


sealed abstract class Table(props: Map[String, String] = Map()) extends Statement {

  def option(key: String): Option[String] = props.get(key)

  val updateMode: Option[String] = props.get("outputMode").orElse(props.get("saveMode")).orElse(props.get("update-mode")).orElse(props.get("updateMode"))

}


case class SourceTable(streaming: Boolean = true, name: String,
                       connector: Connector,
                       schema: Option[Schema] = None,
                       format: RowFormat,
                       props: Map[String, String] = Map())
  extends Table(props) with Serializable {

  override def execute(context: PipelineContext): Unit = SourceTableTranslator(this).translate(context)
}


case class BucketSpec(bucket: Int, columns: Seq[String])

case class SinkTable(streaming: Boolean = true, name: String,
                     schema: Option[Schema] = None,
                     format: Option[RowFormat] = None,
                     connectors: Seq[Connector] = Seq(),
                     partitions: Option[Array[String]] = None,
                     bucket: Option[BucketSpec] = None,
                     props: Map[String, String] = Map())
  extends Table(props) with Serializable {

  override def execute(context: PipelineContext): Unit = SinkTableTranslator(this).translate(context)
}

case class VerifyStatement(name:String,input: String, output: Option[SinkTable],checkLevel: String, constraints: Seq[AssertConstraint]) extends Statement {
  def toCheck = new Check(CheckLevel.withName(checkLevel), name, constraints map (_ toConstraint))

  override def execute(context: PipelineContext): Unit = VerifyStatementTranslator(this).translate(context)
}

sealed trait AssertConstraint {
  def toConstraint: com.amazon.deequ.constraints.Constraint

}

object Assertion {

  def apply[N](operator: String, evaluate: N)(implicit ordered: N => Ordered[N]): N => Boolean = operator match {
    case "==" => _ == evaluate
    case "!=" => _ != evaluate
    case ">=" => _ >= evaluate
    case ">" => _ > evaluate
    case "<=" => _ <= evaluate
    case "<" => _ < evaluate
  }
}

case class SizeConstraint(op: String, count: Long) extends AssertConstraint {
  override def toConstraint: Constraint = sizeConstraint(Assertion(op, count))
}

case class CompleteConstraint(column: String) extends AssertConstraint {
  override def toConstraint: Constraint = completenessConstraint(column, Check.IsOne)
}

case class UniqueConstraint(columns: Seq[String]) extends AssertConstraint {
  override def toConstraint: Constraint = uniquenessConstraint(columns, Check.IsOne)
}

case class SatisfyConstraint(predicate: String, constraintName: String) extends AssertConstraint {
  override def toConstraint: Constraint = complianceConstraint(constraintName, predicate, Check.IsOne)
}

case class DataTypeConstraint(column: String, dataType: String) extends AssertConstraint {
  override def toConstraint: Constraint = dataTypeConstraint(column, ConstrainableDataTypes.withName(dataType), Check.IsOne)
}

case class LengthConstraint(column: String, kind: String, op: String, length: Int) extends AssertConstraint {

  override def toConstraint: Constraint = {
    kind match {
      case "hasMaxLength" => maxLengthConstraint(column, Assertion(op, length.toDouble))
      case "hasMinLength" => minLengthConstraint(column, Assertion(op, length.toDouble))
      case other => throw ParsingException(s"$other is not a valid lengthConstraint")
    }
  }
}


sealed trait ValueConstraint extends AssertConstraint

case class DefaultValueConstraint(kind: String, column: String, op: String, value: Double) extends ValueConstraint {
  override def toConstraint: Constraint = {
    kind match {
      case "hasMin" => minConstraint(column, Assertion(op, value))
      case "hasMax" => maxConstraint(column, Assertion(op, value))
      case "hasMean" => meanConstraint(column, Assertion(op, value))
      case "hasSum" => sumConstraint(column, Assertion(op, value))
      case other => throw ParsingException(s"$other is not a valid ValueConstraint")
    }
  }
}

case class PatternValueConstraint(column: String, pattern: String) extends ValueConstraint {
  override def toConstraint: Constraint = patternMatchConstraint(column, pattern.r, Check.IsOne)
}

case class ApproxQuantileConstraint(column: String,  quantile: Double,op: String,value:Double) extends ValueConstraint {
  override def toConstraint: Constraint = approxQuantileConstraint(column, quantile, Assertion(op, quantile))
}

case class ApproxCountDistinctConstraint(column: String, op: String, value: Double) extends ValueConstraint {
  override def toConstraint: Constraint = approxCountDistinctConstraint(column, Assertion(op, value))
}
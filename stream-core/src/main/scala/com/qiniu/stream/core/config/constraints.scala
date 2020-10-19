package com.qiniu.stream.core.config

import com.amazon.deequ.checks.Check
import com.amazon.deequ.constraints.{ConstrainableDataTypes, StreamConstraints, Constraint => DConstraint}
import com.qiniu.stream.core.exceptions.ParsingException
import com.amazon.deequ.constraints.Constraint._
import com.amazon.deequ.constraints.StreamConstraints._

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

sealed trait Constraint {
  def toConstraint: DConstraint
}

case class SizeConstraint(op: String, count: Long) extends Constraint {
  override def toConstraint: DConstraint = sizeConstraint(Assertion(op, count))
}

case class CompleteConstraint(column: String) extends Constraint {
  override def toConstraint: DConstraint = completenessConstraint(column, Check.IsOne)
}

case class UniqueConstraint(columns: Seq[String]) extends Constraint {
  override def toConstraint: DConstraint = uniquenessConstraint(columns, Check.IsOne)
}

case class SatisfyConstraint(predicate: String, constraintName: String) extends Constraint {
  override def toConstraint: DConstraint = complianceConstraint(constraintName, predicate, Check.IsOne)
}

case class DataTypeConstraint(column: String, dataType: String) extends Constraint {
  override def toConstraint: DConstraint = dataTypeConstraint(column, ConstrainableDataTypes.withName(dataType), Check.IsOne)
}

case class LengthConstraint(column: String, kind: String, op: String, length: Int) extends Constraint {

  override def toConstraint: DConstraint = {
    kind match {
      case "hasMaxLength" => maxLengthConstraint(column, Assertion(op, length.toDouble))
      case "hasMinLength" => minLengthConstraint(column, Assertion(op, length.toDouble))
      case other => throw ParsingException(s"$other is not a valid lengthConstraint")
    }
  }
}


case class MaxMinValueConstraint(kind: String, column: String, op: String, value: Double) extends Constraint {
  override def toConstraint: DConstraint = {
    kind match {
      case "hasMin" => minConstraint(column, Assertion(op, value))
      case "hasMax" => maxConstraint(column, Assertion(op, value))
      case "hasMean" => meanConstraint(column, Assertion(op, value))
      case "hasSum" => sumConstraint(column, Assertion(op, value))
      case other => throw ParsingException(s"$other is not a valid ValueConstraint")
    }
  }
}

case class PatternMatchConstraint(column: String, pattern: String) extends Constraint {
  override def toConstraint: DConstraint = patternMatchConstraint(column, pattern.r, Check.IsOne)
}

case class ApproxQuantileConstraint(column: String,  quantile: Double,op: String,value:Double) extends Constraint {
  override def toConstraint: DConstraint = approxQuantileConstraint(column, quantile, Assertion(op, quantile))
}

case class ApproxCountDistinctConstraint(column: String, op: String, value: Double) extends Constraint {
  override def toConstraint: DConstraint = approxCountDistinctConstraint(column, Assertion(op, value))
}

case class DateFormatConstraint(column:String, format:String) extends Constraint{
  override def toConstraint: DConstraint =  dateFormatConstraint(column,format)
}

case class ExactlyEqualConstraint(table:String) extends Constraint{
  override def toConstraint: DConstraint = ???
}

case class ForeignKeyConstraint(referenceTable:String,column:String, referenceColumn:String) extends Constraint{
  override def toConstraint: DConstraint = ???
}
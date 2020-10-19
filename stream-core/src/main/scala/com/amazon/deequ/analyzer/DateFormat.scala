package com.amazon.deequ.analyzer

import java.text.SimpleDateFormat

import com.amazon.deequ.analyzers.Analyzers.{conditionalCount, conditionalSelection, ifNoNullsIn}
import com.amazon.deequ.analyzers.Preconditions.hasColumn
import com.amazon.deequ.analyzers.{FilterableAnalyzer, NumMatchesAndCount, StandardScanShareableAnalyzer}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StructType}
import org.apache.spark.sql.{Column, Row}

import scala.util.Try

/**
 * Compute approximated count distinct with HyperLogLogPlusPlus.
 *
 * @param column Which column to compute this aggregation on.
 */
case class DateFormat(column: String,formatString:String, where: Option[String] = None)
  extends StandardScanShareableAnalyzer[NumMatchesAndCount]("DateFormat", column)
    with FilterableAnalyzer {

  override def aggregationFunctions(): Seq[Column] = {
    val cannotBeDate = udf((column: String) =>
      column != null && Try {
        val format = new SimpleDateFormat(formatString)
        format.setLenient(false)
        format.parse(column)
      }.isFailure)

    val expression = when(cannotBeDate(col(column)) === lit(false), 1)
      .otherwise(0)

    val summation = sum(conditionalSelection(expression, where).cast(IntegerType))

    summation :: conditionalCount(where) :: Nil

  }

  override def fromAggregationResult(result: Row, offset: Int): Option[NumMatchesAndCount] = {

    ifNoNullsIn(result, offset, howMany = 2) { _ =>
      NumMatchesAndCount(result.getLong(offset), result.getLong(offset + 1))
    }
  }

  override protected def additionalPreconditions(): Seq[StructType => Unit] = {
    hasColumn(column) :: Nil
  }

  override def filterCondition: Option[String] = where
}

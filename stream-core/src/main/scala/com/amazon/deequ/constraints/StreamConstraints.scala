package com.amazon.deequ.constraints

import com.amazon.deequ.analyzer.DateFormat
import com.amazon.deequ.analyzers.NumMatchesAndCount
import com.amazon.deequ.checks.Check

object StreamConstraints {


  /**
   * Runs given date format compliance analysis on the given column and executes the assertion
   *
   * @param name         A name that summarizes the check being made. This name is being used
   *                     to name the metrics for the analysis being done.
   * @param formatString The date format to check compliance for
   * @param column       Data frame column which is a combination of expression and the column name
   * @param hint         A hint to provide additional context why a constraint could have failed
   */
  def dateFormatConstraint(column: String,
                           formatString: String,
                           assert: Double => Boolean = Check.IsOne,
                           where: Option[String] = None,
                           name: Option[String] = None,
                           hint: Option[String] = None): Constraint = {

    val dateFormat = DateFormat(column, formatString, where)

    val constraint = AnalysisBasedConstraint[NumMatchesAndCount, Double, Double](
      dateFormat, assert, hint = hint)

    val constraintName = name match {
      case Some(aName) => aName
      case _ => s"DateFormatConstraint($column, $formatString)"
    }

    new NamedConstraint(constraint, constraintName)
  }

}

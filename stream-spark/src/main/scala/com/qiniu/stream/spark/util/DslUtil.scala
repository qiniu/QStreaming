package com.qiniu.stream.spark.util

import com.qiniu.stream.spark.parser.SqlParser.{PropertyContext, SelectStatementContext}
import org.antlr.v4.runtime.misc.Interval

object DslUtil {

  def parseProperty(pc: PropertyContext): (String, String) = {
    val propKey = cleanQuote(pc.propertyKey.getText)
    val propValue = cleanQuote(pc.propertyValue.getText)
    propKey -> propValue
  }

  def parseSql(selectStatementContext: SelectStatementContext): String = {
    val interval = new Interval(selectStatementContext.start.getStartIndex, selectStatementContext.stop.getStopIndex)
    selectStatementContext.getStart.getInputStream.getText(interval)

  }

  /**
   * clean quote identity
   */
  def cleanQuote(str: String): String = {
    if (isQuoted(str))
      str.substring(1, str.length - 1)
    else str
  }

  private def isQuoted(str: String) = {
    (str.startsWith("`") || str.startsWith("\"") || str.startsWith("'")) && (str.endsWith("`") || str.endsWith("\"") || str.endsWith("'"))
  }
}

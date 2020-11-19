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
package com.qiniu.stream.core.parser

import com.amazon.deequ.checks.Check
import com.qiniu.stream.core.config.Assertion
import com.qiniu.stream.core.parser.SqlParser.{AssertionContext, PropertyContext, SelectStatementContext, TableIdentifierContext}
import org.antlr.v4.runtime.misc.Interval

object ParserHelper {

  def parseProperty(pc: PropertyContext): (String, String) = {
    val propKey = cleanQuote(pc.propertyKey.getText)
    val propValue = cleanQuote(pc.propertyValue.getText)
    propKey -> propValue
  }

  def parseSql(selectStatementContext: SelectStatementContext): String = {
    val interval = new Interval(selectStatementContext.start.getStartIndex, selectStatementContext.stop.getStopIndex)
    selectStatementContext.getStart.getInputStream.getText(interval)

  }

  def parseTableIdentifier(tableIdentifierContext: TableIdentifierContext) = {
    if (tableIdentifierContext.db != null)
      cleanQuote(tableIdentifierContext.db.getText) + "." + cleanQuote(tableIdentifierContext.table.getText)
    else
      cleanQuote(tableIdentifierContext.table.getText)
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
    (str.startsWith("`") && str.endsWith("`")) || (str.startsWith("\"") && str.endsWith("\"")) || (str.startsWith("'") && str.endsWith("'"))
  }


  implicit def dslAssertionToAssertion(ctx: AssertionContext) = {
    if (ctx == null) None else Some(Assertion(ctx.assertionOperator().getText, ctx.value.getText))
  }

}

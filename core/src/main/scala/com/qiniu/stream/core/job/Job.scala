package com.qiniu.stream.core.job

import com.qiniu.stream.core.parser.{SqlBaseListener, SqlLexer, SqlParser}
import com.qiniu.stream.util.Logging
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStream, CommonTokenStream, ParserRuleContext}

import scala.collection.mutable.ArrayBuffer

trait Job extends SqlBaseListener with Logging {

  val statements: ArrayBuffer[Statement] = new ArrayBuffer[Statement]()

  protected def addStatement(statement: Statement): Unit = {
    statements += statement
  }

  protected def printStatement(context: ParserRuleContext): Unit = {
    val statement = {
      val interval = new Interval(context.start.getStartIndex, context.stop.getStopIndex)
      context.start.getInputStream.getText(interval)
    }
    logInfo(s"parsing statement ${statement}")
  }

  protected def parseJob(jobContent: CharStream, params: Map[String, String] = Map()): Unit = {
    val parser = new SqlParser(new CommonTokenStream(new SqlLexer(jobContent)))
    ParseTreeWalker.DEFAULT.walk(this, parser.sql())
  }

}

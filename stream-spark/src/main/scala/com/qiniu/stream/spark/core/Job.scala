package com.qiniu.stream.spark.core

import com.qiniu.stream.spark.config.Statement
import com.qiniu.stream.util.Logging

import scala.collection.mutable.ArrayBuffer

trait Job extends Logging {

  val statements: ArrayBuffer[Statement] = new ArrayBuffer[Statement]()

  protected def addStatement(statement: Statement): Unit = {
    statements += statement
  }

}

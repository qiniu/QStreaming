package com.qiniu.stream.core.job

trait Statement{
  def inDebugMode():Boolean = false
}

/**
 * original sql statement which supported by spark or flink
 * @param sql
 */
case class SqlStatement(sql: String) extends Statement

/**
 * any custom dsl statement should extend from this class
 */
trait DSLStatement extends Statement
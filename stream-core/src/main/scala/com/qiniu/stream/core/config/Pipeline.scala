package com.qiniu.stream.core.config

import scala.collection.mutable.ArrayBuffer

case class Pipeline(statements: ArrayBuffer[Statement] = ArrayBuffer()) {
  def sinkTable(tableName: String): Option[SinkTable] = statements.filter(_.isInstanceOf[SinkTable]).map(_.asInstanceOf[SinkTable]).find(_.name == tableName)
}

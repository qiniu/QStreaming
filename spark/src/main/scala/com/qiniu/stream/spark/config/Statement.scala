package com.qiniu.stream.spark.config

import com.qiniu.stream.core.job.DSLStatement
import com.qiniu.stream.spark.config.ViewType.ViewType

object ViewType extends Enumeration {
  type ViewType = Value
  val persistedView, globalView, tempView = Value
}

case class CreateViewStatement(sql: String, viewName: String, options: Map[String, String] = Map(), viewType: ViewType = ViewType.tempView) extends DSLStatement {
  def showSchema = options.get("showSchema") match {
    case Some(value) => value.equalsIgnoreCase("TRUE")
    case _ => false
  }

  def showTable = options.get("showTable") match {
    case Some(value) => value.equalsIgnoreCase("TRUE")
    case _ => false
  }
  override def inDebugMode(): Boolean = showTable || showSchema
}

case class InsertStatement(sql: String, sinkTable: SinkTable) extends DSLStatement {
  override def inDebugMode(): Boolean = sinkTable.showSchema || sinkTable.showTable
}

case class CreateFunctionStatement(dataType: Option[SqlStructType] = None, funcName: String, funcParam: Option[String], funcBody: String) extends DSLStatement


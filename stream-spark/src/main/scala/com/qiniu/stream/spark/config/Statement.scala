package com.qiniu.stream.spark.config

import com.qiniu.stream.spark.config.ViewType.ViewType


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


sealed abstract class Table(streaming: Boolean,
                            input: Boolean,
                            name: String,
                            connector: Option[Connector] = None,
                            schema: Option[Schema] = None,
                            format: Option[RowFormat] = None,
                            props: Map[String, String] = Map()) extends Statement {

  def option(key: String): Option[String] = props.get(key)

  val updateMode: Option[String] = props.get("outputMode").orElse(props.get("saveMode")).orElse(props.get("update-mode")).orElse(props.get("updateMode"))


  def showSchema: Boolean = option("showSchema") match {
    case Some(value )if value.equalsIgnoreCase("true")=> true
    case _=> false
  }

  def showTable: Boolean = option("showTable") match {
    case Some(value )if value.equalsIgnoreCase("true")=> true
    case _=> false
  }

  override def inDebugMode(): Boolean = showTable || showSchema
}


case class SourceTable(streaming: Boolean = true, name: String,
                       connector: Connector,
                       schema: Option[Schema]=None,
                       format: RowFormat,
                       props: Map[String, String] = Map())
  extends Table(streaming, true, name, Some(connector), schema, Some(format), Map()) with Serializable


case class BucketSpec(bucket: Int, columns: Seq[String])

case class SinkTable(streaming: Boolean = true, name: String,
                     schema: Option[Schema] = None,
                     format: Option[RowFormat] = None,
                     connectors: Seq[Connector] = Seq(),
                     partitions: Option[Array[String]] = None,
                     bucket: Option[BucketSpec] = None,
                     props: Map[String, String] = Map())
  extends Table(streaming, false, name, None, schema, format, props)  with Serializable{


}

package com.qiniu.stream.spark.config

import com.qiniu.stream.core.job.Statement

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

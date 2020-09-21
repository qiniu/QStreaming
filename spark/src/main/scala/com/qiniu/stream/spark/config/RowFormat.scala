package com.qiniu.stream.spark.config

case class RowFormat(name: String, props: Map[String, String] = Map()) {

  def isCsvFormat: Boolean = "CSV".equals(name.toUpperCase)

  def isAvroFormat: Boolean = "AVRO".equals(name.toUpperCase)

  def isRegExFormat: Boolean = "REGEX".equals(name.toUpperCase)

  def isJsonFormat: Boolean = "JSON".equals(name.toUpperCase)

  def isTextFormat: Boolean = "TEXT".equals(name.toUpperCase)

  val rawSchema: Option[String] = props.get("schema")

  def schema: Option[Schema] = rawSchema.map(_.split(",\\s*").map(_.split("\\s+")).filter(_.length == 2).map {
    case Array(fieldName, fieldType) => SchemaField(fieldName, fieldType)
  }).map(Schema(_))

}

object RowFormat {

  def csv(props: Map[String, String] = Map()) = RowFormat("CSV", props)

  def json(props: Map[String, String] = Map()) = RowFormat("JSON", props)

  def avro(props: Map[String, String] = Map()) = RowFormat("AVRO", props)

  def text(props: Map[String, String] = Map()) = RowFormat("TEXT", props)
}



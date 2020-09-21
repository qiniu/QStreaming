package com.qiniu.stream.spark.config

sealed trait TimeField

case class ProcTime(fieldName: String) extends TimeField

case class RowTime(fromField:String, eventTime: String, delayThreadsHold: String) extends TimeField

case class SchemaField(fieldName: String, fieldType: String)

case class Schema(fields: Seq[SchemaField], timeField: Option[TimeField] = None)




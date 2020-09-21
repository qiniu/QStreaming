package com.qiniu.stream.spark.config


trait SqlDataType

object SqlDataType{
  def apply(value:String):SqlDataType ={

    value match {
      case "INTEGER"=> IntDataType()
      case "STRING"=>StringDataType()
      case "BOOLEAN"=>BooleanDataType()
      case "LONG"=>LongDataType()
      case "TINYINT"=>TinyIntDataType()
      case "SMALLINT"=>SmallIntDataType()
      case "TIMESTAMP"=>TimeStampDataType()
      case "DATE"=> DateDataType()
      case "TIME"=>TimeDataType()
      case "DATETIME"=>DateTimeDataType()
      case "DOUBLE"=>DoubleDataType()
      case "FLOAT"=>FloatDataType()
      case "SHORT"=>ShortDataType()
      case "BYTE"=>ByteDataType()
      case "VARCHAR"=>VarcharDataType()
      case _=>StringDataType()
    }
  }
}

case class IntDataType() extends  SqlDataType
case class StringDataType() extends  SqlDataType
case class BooleanDataType() extends  SqlDataType
case class LongDataType() extends  SqlDataType
case class TinyIntDataType() extends  SqlDataType
case class SmallIntDataType() extends  SqlDataType
case class TimeStampDataType() extends  SqlDataType
case class DateDataType() extends  SqlDataType
case class TimeDataType() extends  SqlDataType
case class DateTimeDataType() extends  SqlDataType
case class DoubleDataType() extends  SqlDataType
case class FloatDataType() extends  SqlDataType
case class ShortDataType() extends  SqlDataType
case class ByteDataType() extends  SqlDataType
case class VarcharDataType() extends  SqlDataType

case class SqlField(name:String,dataType: SqlDataType)

case class SqlStructType(fields:Array[SqlField]) extends SqlDataType
package com.qiniu.stream.spark.config

import org.apache.spark.sql.types.StructType

object RichSchema {

  implicit class RichSchema(schema: Schema){
    def structType: StructType = {
      StructType.fromDDL(toDDL)
    }

    def toDDL: String = {
       schema.fields.map(field => s"${field.fieldName} ${field.fieldType}").mkString(",")
    }
  }

}

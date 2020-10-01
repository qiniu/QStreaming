package org.apache.spark.sql.execution.streaming.hbase

import com.alibaba.fastjson.{JSON, JSONObject}
import com.qiniu.stream.util.Logging
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.{DataType, DataTypes, StructType}
import org.joda.time.DateTime
import org.stringtemplate.v4.ST

import scala.util.matching.Regex

case class HBaseRepository(conf: Map[String, String]) extends Logging {

  private val outputTableName: String = conf("tableName")
  private val rowKey: String = conf("rowKey")
  private val family: String = conf("cf")
  private val timeStamp: Option[String] = conf.get("timeStamp")
  private val fields: java.util.List[JSONObject] = {
    JSON.parseArray(conf("fields"), classOf[JSONObject])
  }
  private val pattern: Regex = "<\\s*(.*)\\s*>".r

  private type FieldSchema = (ST, Either[ST, String], DataType)

  require(conf.contains("tableName") &&
    conf.contains("rowKey") &&
    conf.contains("cf") &&
    conf.contains("quorum")
  )

  private def parse(st: ST, row: Row, valueType: Map[String, DataType]): String = {
    valueType.foreach {
      case (field, dataType) ⇒
        dataType match {
          case DataTypes.LongType ⇒ st.add(field, row.getLong(row.fieldIndex(field)))
          case DataTypes.FloatType ⇒ st.add(field, row.getFloat(row.fieldIndex(field)))
          case DataTypes.DoubleType ⇒ st.add(field, row.getDouble(row.fieldIndex(field)))
          case DataTypes.BooleanType ⇒ st.add(field, row.getBoolean(row.fieldIndex(field)))
          case DataTypes.IntegerType ⇒ st.add(field, row.getInt(row.fieldIndex(field)))
          case DataTypes.DateType ⇒ st.add(field, new DateTime(row.getDate(row.fieldIndex(field))).getMillis)
          case DataTypes.TimestampType ⇒ st.add(field, Bytes.toBytes(new DateTime(row.getTimestamp(row.fieldIndex(field))).getMillis))
          case DataTypes.BinaryType ⇒ st.add(field, row.getAs[Array[Byte]](row.fieldIndex(field)))
          case _ ⇒ st.add(field, row.getString(row.fieldIndex(field)))
        }
    }
    st.render()
  }

  private def parse(row: Row, valueName: String, dataType: DataType, valueType: Map[String, DataType]): Array[Byte] = {
    valueType(valueName) match {
      case DataTypes.FloatType ⇒
        if (dataType == DataTypes.FloatType) {
          Bytes.toBytes(row.getFloat(row.fieldIndex(valueName)))
        } else {
          cast(row.getFloat(row.fieldIndex(valueName)).toString, dataType)
        }
      case DataTypes.DoubleType ⇒
        if (dataType == DataTypes.DoubleType) {
          Bytes.toBytes(row.getDouble(row.fieldIndex(valueName)))
        } else {
          cast(row.getDouble(row.fieldIndex(valueName)).toString, dataType)
        }
      case DataTypes.LongType ⇒
        if (dataType == DataTypes.LongType) {
          Bytes.toBytes(row.getLong(row.fieldIndex(valueName)))
        } else {
          cast(row.getLong(row.fieldIndex(valueName)).toString, dataType)
        }
      case DataTypes.IntegerType ⇒
        if (dataType == DataTypes.IntegerType) {
          Bytes.toBytes(row.getInt(row.fieldIndex(valueName)))
        } else {
          cast(row.getInt(row.fieldIndex(valueName)).toString, dataType)
        }
      case DataTypes.BooleanType ⇒
        if (dataType == DataTypes.BooleanType) {
          Bytes.toBytes(row.getBoolean(row.fieldIndex(valueName)))
        } else {
          cast(row.getBoolean(row.fieldIndex(valueName)).toString, dataType)
        }
      case DataTypes.DateType ⇒
        if (dataType == DataTypes.DateType) {
          Bytes.toBytes(new DateTime(row.getDate(row.fieldIndex(valueName))).getMillis)
        } else {
          cast(new DateTime(row.getDate(row.fieldIndex(valueName))).getMillis.toString, dataType)
        }
      case DataTypes.TimestampType ⇒
        if (dataType == DataTypes.DateType) {
          Bytes.toBytes(new DateTime(row.getTimestamp(row.fieldIndex(valueName))).getMillis)
        } else {
          cast(new DateTime(row.getTimestamp(row.fieldIndex(valueName))).getMillis.toString, dataType)
        }
      case DataTypes.BinaryType ⇒ row.getAs[Array[Byte]](row.fieldIndex(valueName))
      case _ ⇒ Bytes.toBytes(row.getString(row.fieldIndex(valueName)))
    }
  }

  private def cast(v: String, d: DataType): Array[Byte] = {
    d match {
      case DataTypes.LongType ⇒ Bytes.toBytes(v.toLong)
      case DataTypes.FloatType ⇒ Bytes.toBytes(v.toFloat)
      case DataTypes.DoubleType ⇒ Bytes.toBytes(v.toDouble)
      case DataTypes.BooleanType ⇒ Bytes.toBytes(v.toBoolean)
      case DataTypes.IntegerType ⇒ Bytes.toBytes(v.toInt)
      case _ ⇒ Bytes.toBytes(v)
    }
  }

  private def convertToPut(schema: StructType, row: Row): (ImmutableBytesWritable, Put) = {
    def resolve(field: JSONObject, valueType: Map[String, DataType]): FieldSchema = {
      val qualified = field.getString("qualified")
      val (value, valueDataType) = field.getString("value") match {
        case pattern(v) ⇒
          (Right(v), valueType.getOrElse(v, DataTypes.StringType))
        case _ ⇒
          (Left(new ST(field.getString("value"))), DataTypes.StringType)
      }

      (new ST(qualified) with Serializable, value, Option.apply(field.getString("type")).map {
        case "StringType" ⇒ DataTypes.StringType
        case "LongType" ⇒ DataTypes.LongType
        case "FloatType" ⇒ DataTypes.FloatType
        case "DoubleType" ⇒ DataTypes.DoubleType
        case "BooleanType" ⇒ DataTypes.BooleanType
        case "IntegerType" ⇒ DataTypes.IntegerType
      }.getOrElse(valueDataType))
    }

    def fetch(f: FieldSchema, row: Row, valueType: Map[String, DataType]): (Array[Byte], Array[Byte]) = {
      f match {
        case (qualified, Left(value), dataType) ⇒
          (parse(qualified, row, valueType).getBytes,
            cast(parse(value, row, valueType), dataType))
        case (qualified, Right(value), dataType) ⇒
          (parse(qualified, row, valueType).getBytes,
            parse(row, value, dataType, valueType))
      }
    }

    val valueType = schema.toArray.map(st => (st.name, st.dataType)).toMap
    val put = new Put(Bytes.toBytes(parse(new ST(rowKey), row, valueType)))
    import scala.collection.convert.wrapAsScala._
    fields.map(field ⇒ fetch(resolve(field, valueType), row, valueType))
      .foreach {
        case (k, v) ⇒
          timeStamp match {
            case Some(ts) ⇒ put.addColumn(Bytes.toBytes(family), k, row.getLong(row.fieldIndex(ts)), v)
            case None ⇒ put.addColumn(Bytes.toBytes(family), k, v)
          }
      }
    (new ImmutableBytesWritable, put)
  }

  def writer(data: DataFrame, overwrite: Boolean): Unit = {
    val hbaseConf = HBaseConfiguration.create()

    hbaseConf.set("hbase.zookeeper.quorum", conf.getOrElse("quorum", "127.0.0.1:2181"))
    for ((key, value) <- conf; if key.startsWith("hbase.")) {
      hbaseConf.set(key, value)
    }

    hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, outputTableName)

    val job = Job.getInstance(hbaseConf)
    job.setOutputFormatClass(classOf[TableOutputFormat[String]])

    val schema = data.schema
    val rdd = data.queryExecution.toRdd.mapPartitions { rows =>
      val converter = CatalystTypeConverters.createToScalaConverter(schema)
      rows.map(converter(_).asInstanceOf[Row])
    }
    rdd.map(convertToPut(schema, _)).saveAsNewAPIHadoopDataset(job.getConfiguration)
  }

}

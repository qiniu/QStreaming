package org.apache.spark.sql.execution.streaming.phoenix

import org.apache.phoenix.schema.types._
import org.apache.phoenix.util.ColumnInfo
import org.apache.spark.sql.types._

package object phoenix {

  implicit class WrapPhoenixColumnInfo(val columnInfo: ColumnInfo) {
    // Lookup table for Phoenix types to Spark catalyst types
    def toDataType: DataType = columnInfo.getPDataType match {
      case t if t.isInstanceOf[PVarchar] || t.isInstanceOf[PChar] => StringType
      case t if t.isInstanceOf[PLong] || t.isInstanceOf[PUnsignedLong] => LongType
      case t if t.isInstanceOf[PInteger] || t.isInstanceOf[PUnsignedInt] => IntegerType
      case t if t.isInstanceOf[PSmallint] || t.isInstanceOf[PUnsignedSmallint] => ShortType
      case t if t.isInstanceOf[PTinyint] || t.isInstanceOf[PUnsignedTinyint] => ByteType
      case t if t.isInstanceOf[PFloat] || t.isInstanceOf[PUnsignedFloat] => FloatType
      case t if t.isInstanceOf[PDouble] || t.isInstanceOf[PUnsignedDouble] => DoubleType
      // Use Spark system default precision for now (explicit to work with < 1.5)
      case t if t.isInstanceOf[PDecimal] =>
        if (columnInfo.getPrecision == null || columnInfo.getPrecision < 0) DecimalType(38, 18) else DecimalType(columnInfo.getPrecision, columnInfo.getScale)
      case t if t.isInstanceOf[PTimestamp] || t.isInstanceOf[PUnsignedTimestamp] => TimestampType
      case t if t.isInstanceOf[PTime] || t.isInstanceOf[PUnsignedTime] => TimestampType
      case t if t.isInstanceOf[PDate] || t.isInstanceOf[PUnsignedDate] => DateType
      case t if t.isInstanceOf[PBoolean] => BooleanType
      case t if t.isInstanceOf[PVarbinary] || t.isInstanceOf[PBinary] => BinaryType
      case t if t.isInstanceOf[PIntegerArray] || t.isInstanceOf[PUnsignedIntArray] => ArrayType(IntegerType, containsNull = true)
      case t if t.isInstanceOf[PBooleanArray] => ArrayType(BooleanType, containsNull = true)
      case t if t.isInstanceOf[PVarcharArray] || t.isInstanceOf[PCharArray] => ArrayType(StringType, containsNull = true)
      case t if t.isInstanceOf[PVarbinaryArray] || t.isInstanceOf[PBinaryArray] => ArrayType(BinaryType, containsNull = true)
      case t if t.isInstanceOf[PLongArray] || t.isInstanceOf[PUnsignedLongArray] => ArrayType(LongType, containsNull = true)
      case t if t.isInstanceOf[PSmallintArray] || t.isInstanceOf[PUnsignedSmallintArray] => ArrayType(IntegerType, containsNull = true)
      case t if t.isInstanceOf[PTinyintArray] || t.isInstanceOf[PUnsignedTinyintArray] => ArrayType(ByteType, containsNull = true)
      case t if t.isInstanceOf[PFloatArray] || t.isInstanceOf[PUnsignedFloatArray] => ArrayType(FloatType, containsNull = true)
      case t if t.isInstanceOf[PDoubleArray] || t.isInstanceOf[PUnsignedDoubleArray] => ArrayType(DoubleType, containsNull = true)
      case t if t.isInstanceOf[PDecimalArray] => ArrayType(
        if (columnInfo.getPrecision == null || columnInfo.getPrecision < 0) DecimalType(38, 18) else DecimalType(columnInfo.getPrecision, columnInfo.getScale), containsNull = true)
      case t if t.isInstanceOf[PTimestampArray] || t.isInstanceOf[PUnsignedTimestampArray] => ArrayType(TimestampType, containsNull = true)
      case t if t.isInstanceOf[PDateArray] || t.isInstanceOf[PUnsignedDateArray] => ArrayType(TimestampType, containsNull = true)
      case t if t.isInstanceOf[PTimeArray] || t.isInstanceOf[PUnsignedTimeArray] => ArrayType(TimestampType, containsNull = true)
    }
  }

}

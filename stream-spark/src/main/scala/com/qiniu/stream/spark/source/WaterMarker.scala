package com.qiniu.stream.spark.source

import com.qiniu.stream.spark.config.RowTime
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DataTypes, LongType, TimestampType}



trait WaterMarker extends Logging {
  def withWaterMark(dataFrame: DataFrame, waterMark: Option[RowTime]): DataFrame = {
    waterMark.map {
      case RowTime(fromField,eventTime, delayThreshold) =>
        val eventTimeColumn = dataFrame.schema.fields.find(_.name == fromField)
        eventTimeColumn.map(_.dataType) match {
          case Some(LongType) =>
            log.info(s"adding watermark. eventTime: $eventTime, type: Long")
            dataFrame.withColumn(eventTime, dataFrame.col(fromField).cast(DataTypes.TimestampType)).withWatermark(eventTime, delayThreshold)
          case Some(TimestampType) =>
            log.info(s"adding watermark. eventTime: $eventTime, type: Timestamp")
            if (eventTime == fromField) {
              dataFrame.withWatermark(eventTime, delayThreshold)
            }else {
              dataFrame.withColumn(eventTime, dataFrame.col(fromField)).withWatermark(eventTime, delayThreshold)
            }
          case _ =>
            log.warn(s"adding watermark. eventTime: $eventTime, type: unknown, no watermark will be added")
            dataFrame
        }
      case _ =>
        log.warn("can't find watermark column, no watermark will be added")
        dataFrame

    }.getOrElse(dataFrame)
  }
}

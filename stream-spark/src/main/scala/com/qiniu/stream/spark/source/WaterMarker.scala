/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

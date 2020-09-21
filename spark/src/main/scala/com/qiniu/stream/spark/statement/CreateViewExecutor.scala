package com.qiniu.stream.spark.statement

import com.qiniu.stream.core.job.JobContext
import com.qiniu.stream.spark.config
import com.qiniu.stream.spark.config.{CreateViewStatement, RowTime, ViewType}
import com.qiniu.stream.spark.source.WaterMarker
import com.qiniu.stream.spark.util.DatasetUtils
import com.qiniu.stream.util.Logging
import org.apache.spark.sql.{DataFrame, SparkSession}

case class CreateViewExecutor(statement: CreateViewStatement) extends StatementExecutor with WaterMarker with Logging {
  val waterMark: Option[RowTime] = statement.options.get("waterMark").map(_.split(",\\s*")) match {
    case Some(Array(fromField, eventTime, delay)) => Some(config.RowTime(fromField, eventTime, delay))
    case Some(Array(eventTime, delay)) => Some(config.RowTime(eventTime, eventTime, delay))
    case _ => None
  }


  def execute(jobContext: JobContext, sparkSession: SparkSession): Unit = {
    log.info(s"parsing query ${statement.sql}")
    statement.viewType match {
      case ViewType.`tempView` => {
        var table = sparkSession.sql(statement.sql)
        table = repartition(table)
        table = withWaterMark(table, waterMark)
        table.createOrReplaceTempView(statement.viewName)
      }
      case ViewType.`globalView` => {
        var table = sparkSession.sql(statement.sql)
        table = repartition(table)
        table = withWaterMark(table, waterMark)
        table.createOrReplaceGlobalTempView(statement.viewName)
      }
      case ViewType.`persistedView` => {
        sparkSession.sql(s"create view ${statement.viewName} as ${statement.sql}")
      }
    }
    //for debug purpose
    if (statement.showSchema) {
      sparkSession.table(statement.viewName).printSchema()
    }
    //for debug purpose
    if (statement.showTable) {
      val table = sparkSession.table(statement.viewName)
      DatasetUtils.showTable(table)
    }
  }


  private def repartition(table: DataFrame): DataFrame = {
    var dataFrame = statement.options.get("repartition") match {
      case Some(repartition) =>
        val partitionColumns = repartition.split(",\\s*")
        partitionColumns match {
          case Array(num) if num.forall(_.isDigit) =>
            log.debug("Repartitioned with size. repartition={}", repartition)
            table.repartition(num.toInt)
          case Array(num, tails@_*) if num.forall(_.isDigit) =>
            log.debug("Repartitioned with fix size and columns. repartition={}", repartition)
            table.repartition(num.toInt, tails.map(table.col): _*)
          case Array() =>
            log.debug("Repartition option with no effect. repartition={}", repartition)
            table
          case allColumns =>
            log.debug("Repartitioned with columns. repartition={}", repartition)
            table.repartition(allColumns.map(table.col): _*)
        }
      case None =>
        log.debug("No repartition")
        table
    }

    val coalesce: Option[Int] = statement.options.get("coalesce").map(_.toInt)
    dataFrame = coalesce.map(dataFrame.coalesce).getOrElse(dataFrame)

    dataFrame
  }
}

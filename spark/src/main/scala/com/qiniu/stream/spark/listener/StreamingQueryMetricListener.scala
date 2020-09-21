package com.qiniu.stream.spark.listener

import com.qiniu.stream.util.Logging
import org.apache.spark.groupon.metrics.UserMetricsSystem
import org.apache.spark.sql.streaming.StreamingQueryListener

class StreamingQueryMetricListener extends StreamingQueryListener with Logging {

  override def onQueryStarted(event: StreamingQueryListener.QueryStartedEvent): Unit = {
    //NO OP
  }

  override def onQueryProgress(event: StreamingQueryListener.QueryProgressEvent): Unit = {
    val numInputRows = event.progress.numInputRows
    UserMetricsSystem.gauge( "InputEventsCount").set(numInputRows)

    val processedRowsPerSecond = event.progress.processedRowsPerSecond
    UserMetricsSystem.gauge("ProcessedEventsPerSecond").set(processedRowsPerSecond.toLong)
  }

  override def onQueryTerminated(event: StreamingQueryListener.QueryTerminatedEvent): Unit = {

    event.exception match {
      case Some(e) =>
        UserMetricsSystem.counter("QueryExceptionCounter").inc()
        log.error("Query failed with exception: " + e)
      case None =>
        UserMetricsSystem.counter("QueryStopCounter").inc()
    }
  }
}
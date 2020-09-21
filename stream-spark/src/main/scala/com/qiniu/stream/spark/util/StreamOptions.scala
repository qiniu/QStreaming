package com.qiniu.stream.spark.util

object StreamOptions {
  val updateMode = "update-mode"
  val batchWrite = "batchWrite"
  val triggerMode = "triggerMode"
  val triggerModeOnce = "Once"
  val triggerModeContinuous = "Continuous"
  val triggerModeProcessingTime = "ProcessingTime"
  val triggerInterval = "triggerInterval"
  val queryName = "queryName"
  val checkpointLocation = "checkpointLocation"
}

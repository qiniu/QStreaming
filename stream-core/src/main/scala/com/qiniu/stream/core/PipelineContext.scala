package com.qiniu.stream.core

import com.qiniu.stream.core.PipelineConfig.DEBUG_MODE
import com.typesafe.config.Config

case class PipelineContext(job: PipelineListener, config: Config, params: collection.mutable.Map[String, String] = collection.mutable.HashMap()) {



  def set(key: String, value: String): Unit = {
    params + (key -> value)
  }

  def setDebug(value: Boolean) = {
    set(DEBUG_MODE, "true")
  }

  def isDebugMode: Boolean = {
    params.get(DEBUG_MODE) match {
      case Some(value) if (value.equalsIgnoreCase("true")) => true
      case _ => false
    }
  }
}

package com.qiniu.stream.core.job

import com.typesafe.config.Config

case class JobContext(job: Job, config: Config, params: collection.mutable.Map[String, String] = collection.mutable.HashMap()) {

  def set(key: String, value: String): Unit = {
    params + (key -> value)
  }

  def setDebug(value: Boolean) = {
    set("debug", "true")
  }

  def isDebugMode: Boolean = {
    params.get("debug") match {
      case Some(value) if (value.equalsIgnoreCase("true")) => true
      case _ => false
    }
  }
}

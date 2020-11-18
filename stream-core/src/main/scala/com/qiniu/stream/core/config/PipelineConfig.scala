package com.qiniu.stream.core.config

import scala.io.Source

case class PipelineConfig(pipeline: Source, settings: Settings = Settings.load(), jobVariables: Map[String, String] = Map())

object PipelineConfig {

  val DEFAULT: PipelineConfig ={
    val resourceIs = PipelineConfig.getClass.getClassLoader.getResourceAsStream("job.dsl")
    PipelineConfig(Source.fromInputStream(resourceIs))
  }

  def fromClassPath(resource: String, settings: Settings, jobVariables: Map[String, String]): PipelineConfig = {
    val resourceIs = PipelineConfig.getClass.getClassLoader.getResourceAsStream(resource)
    PipelineConfig(Source.fromInputStream(resourceIs), settings, jobVariables)
  }
}



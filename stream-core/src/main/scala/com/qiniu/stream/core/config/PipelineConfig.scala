package com.qiniu.stream.core.config

sealed trait PipelineConfig {
  def args: Map[String,String]
}

case class FilePipelineConfig(jobDsl: String = "job.dsl", args: Map[String, String] = Map()) extends PipelineConfig

case class ResourcePipelineConfig(jobResource: String , args: Map[String, String] = Map()) extends PipelineConfig


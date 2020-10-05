package com.qiniu.stream.core.translator

import com.qiniu.stream.core.PipelineContext
import com.qiniu.stream.core.config.Pipeline
import com.qiniu.stream.util.Logging

class PipelineTranslator(pipeline: Pipeline) extends StatementTranslator with Logging {
  override def translate(pipelineContext: PipelineContext): Unit = pipeline.statements.foreach(_.execute(pipelineContext))
}

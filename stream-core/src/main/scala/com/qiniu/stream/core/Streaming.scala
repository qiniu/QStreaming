package com.qiniu.stream.core

import com.qiniu.stream.core.config.Settings
import com.qiniu.stream.core.parser.PipelineParser
import scopt.OptionParser

object Streaming extends App {

  case class StreamingConfig(jobFile: Option[String] = None, templateVariables: Map[String, String] = Map()) {
    def asSettings: Settings = {
      val settings = jobFile.map(Settings load _).getOrElse(Settings load())
      templateVariables.foreach {
        case (k, v) => settings.withValue(k, v)
      }
      settings
    }
  }


  val cliParser: OptionParser[StreamingConfig] = new OptionParser[StreamingConfig]("QStreaming") {
    head("QStreaming")

    opt[String]('c', "config")
      .text("Path to the job dsl file")
      .action((file, c) => c.copy(jobFile = Option(file)))

    opt[Map[String, String]]("vars").valueName("k1=v1, k2=v2...")
      .action((vars, c) => c.copy(templateVariables = vars))
      .text("template variables")

    help("help") text "use command line arguments to specify the configuration file path or content"
  }

  def run(streamConfig: StreamingConfig): Unit = {
    val pipelineContext = PipelineContext(streamConfig.asSettings)
    val pipeline = new PipelineParser(pipelineContext).parse()
    PipelineRunner(pipelineContext).run(pipeline)
  }

  cliParser.parse(args, StreamingConfig()) match {
    case Some(config) => run(config)
    case None => run(StreamingConfig())
  }

}

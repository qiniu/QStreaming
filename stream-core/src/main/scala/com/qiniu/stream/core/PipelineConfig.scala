package com.qiniu.stream.core

import com.typesafe.config.Config

import scala.io.Source

/**
 * Enrich the typesafe Config
 */
object PipelineConfig {
  val PIPELINE_CONF = "pipeline.conf"
  val PIPELINE_DSL = "pipeline.dsl"
  val DEBUG_MODE = "debug"

  private val TEMPLATE_START_CHAR = "st.start.char"
  private val TEMPLATE_STOP_CHAR = "st.stop.char"
  private val TEMPLATE_DISABLE = "st.disable"

  private val DEFAULT_START_CHAR = '['
  private val DEFAULT_STOP_CHAR = ']'

  implicit class RichConfig(config: Config) {

    lazy val source = Source.fromFile("pipeline.dsl")

    def getOptionalString(key: String): Option[String] = {
      if (config.hasPath(key)) {
        Some(config.getString(key))
      } else None
    }

    def getString(key: String, default: String): String = getOptionalString(key).getOrElse(default)


    def getOptionalBoolean(key: String): Option[Boolean] = {
      if (config.hasPath(key)) {
        Some(config.getBoolean(key))
      } else None
    }

    def getBoolean(key: String, default: Boolean): Boolean = getOptionalBoolean(key).getOrElse(default)


    def getOptionalInt(key: String): Option[Int] = {
      if (config.hasPath(key)) {
        Some(config.getInt(key))
      } else None
    }

    def getInt(key: String, default: Int): Int = getOptionalInt(key).getOrElse(default)


    def getOptionalLong(key: String): Option[Long] = {
      if (config.hasPath(key)) {
        Some(config.getLong(key))
      } else None
    }

    def getLong(key: String, default: Long): Long = getOptionalLong(key).getOrElse(default)

    def getOptionalDouble(key: String): Option[Double] = {
      if (config.hasPath(key)) {
        Some(config.getDouble(key))
      } else None
    }

    def getDouble(key: String, default: Double): Double = getOptionalDouble(key).getOrElse(default)

    def isTemplateEnable: Boolean = config.hasPath(TEMPLATE_DISABLE) && config.getBoolean(TEMPLATE_DISABLE)

    def templateStartStopChar: (Char, Char) = {

      val startChar = if (config.hasPath(TEMPLATE_START_CHAR)) {
        config.getString(TEMPLATE_START_CHAR).charAt(0)
      } else {
        DEFAULT_START_CHAR
      }
      val stopChar = if (config.hasPath(TEMPLATE_STOP_CHAR)) {
        config.getString(TEMPLATE_STOP_CHAR).charAt(0)
      } else {
        DEFAULT_STOP_CHAR
      }
      (startChar, stopChar)
    }

    def isDebugEnable: Boolean = config.hasPath(DEBUG_MODE) && config.getString(DEBUG_MODE).equalsIgnoreCase("true")

  }

}

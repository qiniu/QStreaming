/*
 * Copyright 2020 Qiniu Cloud (qiniu.com)
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qiniu.stream.spark.core

import com.typesafe.config.Config

import scala.io.Source

/**
 * Enrich the typesafe Config
 */
object PipelineConfig {
  private val TEMPLATE_START_CHAR = "st.start.char"
  private val TEMPLATE_STOP_CHAR = "st.stop.char"
  private val TEMPLATE_DISABLE = "st.disable"
  private val debugMode = "debug"
  private val DEFAULT_START_CHAR = '['
  private val DEFAULT_STOP_CHAR = ']'


  implicit class RichConfig(config: Config) {

    lazy val source = Source.fromFile("job.dsl")

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

    def isDebugEnable: Boolean = config.hasPath(debugMode) && config.getString(debugMode).equalsIgnoreCase("true")

  }

}

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
package com.qiniu.stream.core.config

import java.util.concurrent.TimeUnit

import com.qiniu.stream.core.config.Settings.Key
import com.qiniu.stream.core.exceptions.SettingsValidationException
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{Success, Try}

class Settings(val config: Config) {
  def apply[T](key: Key[T]): T = key.validator(key.get(config)).recover {
    case NonFatal(cause) =>
      throw SettingsValidationException(
        s"Configured value of settings key ${key.name} didn't pass validation: ${cause.getMessage}",
        cause
      )
  }.get


  def withValue(key: String, value: AnyRef): Settings =
    new Settings(config.withValue(key, ConfigValueFactory.fromAnyRef(value)))

  def withValue[T](key: Key[T], value: T): Settings =
    withValue(key.name, value.asInstanceOf[AnyRef])
}

object Settings {

  case class Key[T](name: String, get: Config => T, validator: T => Try[T] = Success(_: T)) {
    def validate(validator: T => Try[T]): Key[T] = copy(validator = validator)

    override def toString: String = name
  }

  object Key {

    case class KeyBuilder(name: String) {
      def boolean: Key[Boolean] = Key[Boolean](name, _ getBoolean name)

      def number: Key[Number] = Key[Number](name, _ getNumber name)

      def string: Key[String] = Key[String](name, _ getString name)

      def char: Key[Char] = Key[Char](name, _.getString(name).charAt(0))

      def int: Key[Int] = Key[Int](name, _ getInt name)

      def long: Key[Long] = Key[Long](name, _ getLong name)

      def double: Key[Double] = Key[Double](name, _ getDouble name)

      def anyRef: Key[AnyRef] = Key[AnyRef](name, _ getAnyRef name)

      private def duration(config: Config, unit: TimeUnit): Long = config getDuration(name, unit)

      def nanos: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.NANOSECONDS).nanos)

      def micros: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.MICROSECONDS).micros)

      def millis: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.MILLISECONDS).millis)

      def seconds: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.SECONDS).seconds)

      def minutes: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.MINUTES).minutes)

      def hours: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.HOURS).hours)

      def days: Key[Duration] = Key[Duration](name, duration(_, TimeUnit.DAYS).days)


    }

    def apply(name: String): KeyBuilder = KeyBuilder(name)
  }

  val empty: Settings = new Settings(ConfigFactory.empty())

  def apply(config: Config): Settings = new Settings(config)

  def load(): Settings = Settings(
    ConfigFactory
      // Environment variables takes highest priority and overrides everything else
      .systemEnvironment()
      // System properties comes after environment variables
      .withFallback(ConfigFactory.systemProperties())
      // Configurations of all other components (like Akka)
      .withFallback(ConfigFactory.load())
      .resolve()
  )

}


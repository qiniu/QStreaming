/*
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
package com.qiniu.stream.spark.util

import com.alibaba.fastjson.JSONObject
import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.qiniu.stream.util.Logging

import scala.util.matching.Regex

object Regex2Json extends Logging {
  //LoadingCache[(pattern,groupNames),RegEx]
  type PatternGroupNames = (String, String)
  val regexCache: LoadingCache[PatternGroupNames, Regex] = CacheBuilder.newBuilder.build(new CacheLoader[PatternGroupNames, Regex] {
    override def load(key: PatternGroupNames): Regex = {
      val fieldNames = asSchema(key._2).map(_._1).toArray
      new Regex(key._1, fieldNames: _*)
    }
  })

  private def asSchema(valueSchema: String): List[(String, String)] = {
    valueSchema.split(",").map { field =>
      val kv = field.split("\\s+")
      (kv(0), kv(1))
    }.toList
  }


  def toJson(line: String, patten: String, valueSchema: String): String = {
    val fields = asSchema(valueSchema)

    val regex = regexCache.get((patten, valueSchema))
    //    val regex = new Regex(patten, fieldNames: _*)
    regex findFirstMatchIn line match {
      case Some(find) =>
        val node = new JSONObject()
        try {
          fields.foreach { kv =>
            kv._2.toLowerCase match {
              case "long" =>
                node.put(kv._1, find.group(kv._1).toLong)
              case "string" =>
                node.put(kv._1, find.group(kv._1).toString)
              case "int" | "integer" =>
                node.put(kv._1, find.group(kv._1).toInt)
              case "boolean" =>
                node.put(kv._1, find.group(kv._1).toBoolean)
              case "byte" =>
                node.put(kv._1, find.group(kv._1).toByte)
              case "short" =>
                node.put(kv._1, find.group(kv._1).toShort)
              case "float" =>
                node.put(kv._1, find.group(kv._1).toFloat)
              case "double" =>
                node.put(kv._1, find.group(kv._1).toDouble)
              case "decimal" =>
                node.put(kv._1, find.group(kv._1).toDouble)
              case _ =>
                node.put(kv._1, find.group(kv._1).toString)
            }
          }
          node.toString
        } catch {
          case e : Exception => {
            log.warn(s"Failed to parse regex: $line", e)
            "{}"
          }
        }
      case None =>
        "{}"
    }
  }
}
package com.qiniu.stream.spark.config

case class Connector(name: String, options: Map[String, String] = Map()) {

  def isKafka: Boolean = name.toLowerCase == "kafka"

  def isConsole: Boolean = name.toLowerCase == "console"

  def isMemory: Boolean = name.toLowerCase() == "memory"

  def isHBase: Boolean = name.toLowerCase() == "hbase"

  def isCustom: Boolean = name.toLowerCase() == "custom"

  def option(key: String): Option[String] = options.get(key)

  def conditionExpr: Option[String] = option("where")

  def reader: Option[String] = option("reader")

  def includeColumns: Option[Array[String]] = option("selectExpr").orElse(option("include-columns")).map(_.split(",\\s*"))

  def excludeColumns: Option[Array[String]] = option("dropColumns").orElse(option("exclude-columns")).map(_.split(",\\s*"))

  def outputMode: Option[String] = option("outputMode").orElse(option("update-mode")).orElse(option("saveMode"))

  lazy val partitions: Option[Array[String]] = options.get("partitions").map(_.split(",\\s*"))

  lazy val buckets: Option[BucketSpec] = options.get("buckets").map(_.split("|\\s*")) match {
    case Some(Array(bucket, bucketColumns)) => Some(BucketSpec(bucket.toInt, bucketColumns.split(",\\s*")))
    case None => None

  }

}

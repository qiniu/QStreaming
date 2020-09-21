package com.qiniu.stream.spark.core

import com.qiniu.stream.spark.listener.StreamingQueryMetricListener
import com.qiniu.stream.util.Logging
import com.typesafe.config.Config
import org.apache.spark.SparkConf
import org.apache.spark.groupon.metrics.UserMetricsSystem
import org.apache.spark.sql.SparkSession

object SparkSessionFactory extends Logging {

  def load(config: Config) = {
    val sparkConf: SparkConf = {
      val sparkConf = new SparkConf()
      if (config.hasPath("spark")){
        import scala.collection.JavaConversions._
        config.getConfig("spark").entrySet().foreach(e => {
          sparkConf.set(e.getKey, config.getString("spark." + e.getKey))
        })
      }
      sparkConf
    }

    log.info("init spark session")
    //set master if possible
    val session = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    log.info("register functions.....")
//    DefaultUdfRegister.register(session.udf)
//    UdfRegister(session.udf)

    log.info("register streaming listener...")
    UserMetricsSystem.initialize(session.sparkContext)
    session.streams.addListener(new StreamingQueryMetricListener)
    session

  }


}

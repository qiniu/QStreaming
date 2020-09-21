package com.qiniu.stream.spark.listener

import java.util
import java.util.Properties

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer, OffsetAndMetadata}
import org.apache.kafka.common.TopicPartition
import org.apache.spark.sql.execution.QueryExecutionException
import org.apache.spark.sql.streaming.StreamingQueryListener
import org.apache.spark.sql.streaming.StreamingQueryListener._

/**
  * a dummy kafka consumer and update partition offset to a dummy group id
  *
  * @param config
  */
class KafkaLagListener(groupId: String, bootStrapServer: String) extends StreamingQueryListener with Logging {
  private val consumer = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    new KafkaConsumer[String, String](props)
  }

  private val om = {
    val objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }

  def onQueryStarted(event: QueryStartedEvent): Unit = {}

  def onQueryTerminated(event: QueryTerminatedEvent): Unit = {}

  def onQueryProgress(event: QueryProgressEvent): Unit = {
    try {
      event.progress.sources.foreach(source => {

        val jsonOffsets = om.readValue(source.endOffset, classOf[Map[String, Map[String, Any]]])
        jsonOffsets.keys.foreach(topic => {
          val topicPartitionMap = new util.HashMap[TopicPartition, OffsetAndMetadata]()
          val offsets = jsonOffsets.get(topic)
          offsets match {
            case Some(topicOffsetData) =>
              topicOffsetData.keys.foreach(partition => {
                val tp = new TopicPartition(topic, partition.toInt)
                val oam =  topicOffsetData(partition) match {
                  case partition:Long => new OffsetAndMetadata(partition)
                  case partition:Int => new OffsetAndMetadata(partition)
                  case partition:String => new OffsetAndMetadata(partition.toLong)
                }
                topicPartitionMap.put(tp, oam)
              })
            case _ =>
              throw new QueryExecutionException("could not fetch topic offsets")
          }
          consumer.commitSync(topicPartitionMap)

        })
      })
    } catch {
      case e: Exception => log.error(s"Error happen while committed offset to consumer group for $groupId", e)
    }
  }


}



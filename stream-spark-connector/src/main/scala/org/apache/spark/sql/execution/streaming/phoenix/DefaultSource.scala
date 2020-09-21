package org.apache.spark.sql.execution.streaming.phoenix

import com.qiniu.stream.util.Logging
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.phoenix.spark.PhoenixRelation
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, RelationProvider}
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.phoenix.spark._

class DefaultSource extends RelationProvider with CreatableRelationProvider with Logging {

  // Override 'RelationProvider.createRelation', this enables DataFrame.load()
  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    verifyParameters(parameters)

    new PhoenixRelation(
      parameters("table"),
      parameters("zkUrl"),
      parameters.contains("dateAsTimestamp")
    )(sqlContext)
  }

  // Override 'CreatableRelationProvider.createRelation', this enables DataFrame.save()
  override def createRelation(sqlContext: SQLContext, mode: SaveMode,
                              parameters: Map[String, String], data: DataFrame): BaseRelation = {
    verifyParameters(parameters)

    val config = HBaseConfiguration.create()
    parameters.foreach{
      case (k,v)=> config.set(k, v)
    }
    config.set("phoenix.mutate.batchSize","5000")

    config.set("phoenix.upsert.batch.size","15000")
    printConfig(config)

    // Save the DataFrame to Phoenix
    data.saveToPhoenix(parameters("table"),config, Some(parameters("zkUrl")))

    // Return a relation of the saved data
    createRelation(sqlContext, parameters)
  }

  private def printConfig(config: Configuration): Unit = {
    log.info("createRelation properties")

    val itr = config.iterator()



    while (itr.hasNext) {
      val entry = itr.next();
      log.info(s"${entry.getKey}=${entry.getValue}")
    }
  }

  // Ensure the required parameters are present
  def verifyParameters(parameters: Map[String, String]): Unit = {
    if (!parameters.contains("table")) throw new RuntimeException("No Phoenix 'table' option defined")
    if (!parameters.contains("zkUrl")) throw new RuntimeException("No Phoenix 'zkUrl' option defined")
  }

}

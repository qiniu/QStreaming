package org.apache.spark.sql.execution.streaming.hbase


import org.apache.spark.sql.sources._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

class DefaultSource extends RelationProvider with CreatableRelationProvider {

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation =
    HBaseRelation(parameters, None)(sqlContext)


  override def createRelation(sqlContext: SQLContext, mode: SaveMode, parameters: Map[String, String], data: DataFrame): BaseRelation = {
    val relation = HBaseInsertRelation(data, parameters)(sqlContext)
    relation.insert(data, mode == SaveMode.Overwrite)
    relation
  }
}

case class HBaseInsertRelation(
                                dataFrame: DataFrame,
                                parameters: Map[String, String]
                              )(@transient val sqlContext: SQLContext)
  extends BaseRelation with InsertableRelation {

  override def insert(data: DataFrame, overwrite: Boolean): Unit = {
    val repository = HBaseRepository(parameters)
    repository.writer(dataFrame, overwrite)
  }

  override def schema: StructType = dataFrame.schema
}

case class HBaseRelation(
                          parameters: Map[String, String],
                          userSpecifiedschema: Option[StructType]
                        )(@transient val sqlContext: SQLContext)
  extends BaseRelation {

  override def schema: StructType = {
    import org.apache.spark.sql.types._
    StructType(
      Array(
        StructField("rowkey", StringType, false),
        StructField("content", StringType)
      )
    )

  }
}
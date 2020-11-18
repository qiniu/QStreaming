package com.qiniu.stream.sql.elasticsearch

import org.elasticsearch.spark.sql.DefaultSource15

/**
 * add this new source provider for streaming elasticsearch
 */
class ElasticSearchSourceProvider extends DefaultSource15 with org.apache.spark.sql.sources.DataSourceRegister{
  override def shortName(): String = "streaming-es"
}

### Input
Syntax

```sql
CREATE BATCH INPUT TABLE table_identifier USING org.apache.hadoop.hbase.spark(catalog=<catalog>);
```

Parameters:

- table_identifier - name of the input table
- catalog:  catalog of the hbase table

Example:

```sql
CREATE BATCH INPUT TABLE hbaseTable USING org.apache.hadoop.hbase.spark(catalog='{
	"table":{"namespace":"default", "name":"HBaseSourceExampleTable"},
	"rowkey":"key",
	"columns":{
		"col0":{"cf":"rowkey", "col":"key", "type":"string"},
		"col1":{"cf":"cf1", "col":"col1", "type":"boolean"},
		"col2":{"cf":"cf2", "col":"col2", "type":"double"},
		"col3":{"cf":"cf3", "col":"col3", "type":"float"},
		"col4":{"cf":"cf4", "col":"col4", "type":"int"},
		"col5":{"cf":"cf5", "col":"col5", "type":"bigint"},
		"col6":{"cf":"cf6", "col":"col6", "type":"smallint"},
		"col7":{"cf":"cf7", "col":"col7", "type":"string"},
		"col8":{"cf":"cf8", "col":"col8", "type":"tinyint"}
	}
}');
```

### Output

Syntax

```sql
,#batch
CREATE BATCH OUTPUT TABLE table_identifier USING org.apache.hadoop.hbase.spark(catalog=<catalog>,hbase.spark.config.location=<hbaseSiteXmlLocation>);

#streaming
CREATE STREAM OUTPUT TABLE table_identifier USING streaming-hbase(catalog=<catalog>,hbase.zookeeper.quorum=<zkQuorum>, hbase.zookeeper.property.clientPort=<zkClientPort>,,hbase.spark.config.location=<hbaseSiteXmlLocation>) TBLPROPERTIES(checkpointLocation=<checkPointLocation>);

```

Parameters:

- table_identifier - name of the input table
- catalog:  catalog of the hbase table
- zkQuorum - zookeeper quorum host ( only used in streaming)
- zkClientPort - zookeeper client port  ( only used in streaming)
- hbaseSiteXmlLocation - location of hbase site xml ( optional for streaming)

Example:

```sql
CREATE BATCH OUTPUT TABLE hbaseTable USING org.apache.hadoop.hbase.spark(catalog='{
  "table":{
  	"namespace":"default",
  	"name":"htable"},
    "rowkey":"key1:key2",
    "columns":{
       "col1":{"cf":"rowkey", "col":"key1", "type":"string"},
       "col2":{"cf":"rowkey", "col":"key2", "type":"double"},
       "col3":{"cf":"cf1", "col":"col2", "type":"binary"},
       "col4":{"cf":"cf1", "col":"col3", "type":"timestamp"},
       "col5":{"cf":"cf1", "col":"col4", "type":"double", "serdes":"${classOf[DoubleSerDes].getName}"},
       "col6":{"cf":"cf1", "col":"col5", "type":"$map"},
       "col7":{"cf":"cf1", "col":"col6", "type":"$array"},
       "col8":{"cf":"cf1", "col":"col7", "type":"$arrayMap"}
    }
}');

#streaming
create stream output table outputTable using streaming-hbase(
  catalog='{
      "table":{
    	"namespace":"default",
    	"name":"test"
      },
      "rowkey":"key1",
      "columns":{
         "name":{"cf":"rowkey", "col":"key1", "type":"string"},
         "value":{"cf":"cf", "col":"value", "type":"int"}
      }
  }', hbase.zookeeper.quorum="localhost", hbase.zookeeper.property.clientPort="2181"
 ) TBLPROPERTIES(checkpointLocation="/tmp/checkpoint/hbase");
```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu-stream-hbase:0.1.0 \
stream-standalone-0.0.4-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


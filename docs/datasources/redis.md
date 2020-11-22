### Input
Syntax

```sql
#batch
CREATE BATCH INPUT TABLE table_identifier(fieldName1 fieldType1, fieldName2 fieldType2,...) USING org.apache.spark.sql.redis(table=<table>,key.column=<keyColumn>,ttl=<ttl>);

#streaming ( integrate with redis streaming)
CREATE STREAM INPUT TABLE table_identifier(fieldName1 fieldType1, fieldName2 fieldType2,...) USING redis(stream.keys=<streamKey>);
```

Parameters:

- batch mode
  - table_identifier - name of the input table
  - table -  redis hash key name 
  - keyColumn - the key name of redis hash
  - fieldName - stream field name
  - fieldType - stream field type
  - ttl(optional) - Time to live (If you want to expire you data after a certain period of time )

- streaming mode
  - table_identifier - name of the input table
  - fieldName - stream field name
  - fieldType - stream field type
  - streamKey - redis stream key 

Example:

- batch

```sql
CREATE BATCH INPUT person( name STRING , age INT) USING org.apache.spark.sql.redis(table = "person", key.colum= "name");
```

- streaming

```sql
CREATE STREAM INPUT TABLE sensor("sensor-id" STRING,temperature FLOAT) USING redis(stream.keys="sensors");
```

You can write the following items to the stream to test it:

```
xadd sensors * sensor-id 1 temperature 28.1
xadd sensors * sensor-id 2 temperature 30.5
xadd sensors * sensor-id 1 temperature 28.3
```

### Output

Syntax:

```sql
#batch 
CREATE BATCH output table_identifier( name STRING , age INT) USING org.apache.spark.sql.redis(table = <table>, key.colum= <keyColumn>);

//streaming
CREATE STREAM OUTPUT TABLE table_identifier USING redis(table=<table> ) TBLPROPERTIES(checkpointLocation=<checkPointLocation>,outputMode="append");
```

Parameters:

- table - table name of cassandra
- keyspace - key space of cassandra
- cluster - cluster name of es cassandra

Examples:

```sql
#batch
create batch output table person using org.apache.spark.sql.redis(table="person",key.column="name");

#streaming
create stream output table sensor using redis(table="output") TBLPROPERTIES(checkpointLocation=<checkPointLocation>,outputMode="append");
```

### spark-submit

#### shell command

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--conf spark.redis.host localhost
--conf spark.redis.port 6379
--packages com.qiniu:stream-redis:0.1.0  \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```

#### spark Context configuration parameters

- `spark.redis.host` - host or IP of the initial node we connect to. The connector will read the cluster topology from the initial node, so there is no need to provide the rest of the cluster nodes.
- `spark.redis.port` - the initial node's TCP redis port.
- `spark.redis.auth` - the initial node's AUTH password
- `spark.redis.db` - optional DB number. Avoid using this, especially in cluster mode.
- `spark.redis.timeout` - connection timeout in ms, 2000 ms by default
- `spark.redis.max.pipeline.size` - the maximum number of commands per pipeline (used to batch commands). The default value is 100.
- `spark.redis.scan.count` - count option of SCAN command (used to iterate over keys). The default value is 100.
- `spark.redis.ssl` - set to true to use tls
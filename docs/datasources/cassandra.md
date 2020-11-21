### Input
Syntax

```sql
CREATE BATCH INPUT TABLE table_identifier USING org.apache.spark.sql.cassandra(table=<table>, keyspace=<keyspace>, cluster=<cluster>);
```

Parameters:

- table_identifier - name of the input table
- table:  table name of cassandra
- Keyspace - key space of cassandra
- cluster - cluster of cassandra

Example:

```sql
CREATE BATCH INPUT TABLE hbaseTable USING org.apache.spark.sql.cassandra(table="user",keyspace="test", cluster="cluster_A");
```

### Output
Syntax:

```sql
#batch
CREATE BATCH OUTPUT TABLE table_identifier USING org.apache.spark.sql.cassandra(table=<table>,keyspace=<keyspace>,cluster=<cluster>);

//streaming
CREATE STREAM OUTPUT TABLE table_identifier USING org.apache.spark.sql.cassandra(table=<table>,keyspace=<keyspace>,cluster=<cluster>) TBLPROPERTIES(checkpointLocation=<checkPointLocation>);
```

Parameters:

- table - table name of cassandra
- keyspace - key space of cassandra
- cluster - cluster name of es cassandra

Examples:

```sql
#batch
create batch output table dogs using
org.apache.spark.sql.cassandra(table="user",keyspace="test",cluster="cluster_a");

#streaming
create stream output table dogs using
org.apache.spark.sql.cassandra(table="user",keyspace="test",cluster="cluster_a") TBLPROPERTIES(checkpointLocation=<checkPointLocation>);
```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-cassandra:0.0.4  \
stream-standalone-0.0.4-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


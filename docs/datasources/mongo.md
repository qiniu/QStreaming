### Input
Syntax

```sql
CREATE BATCH INPUT TABLE table_identifier USING mongo(uri=<mongoUri>, database=<database>,collection=<collection>);
```

Parameters:

- table_identifier -   table name of input table.
- [mongoUri](https://docs.mongodb.com/manual/reference/connection-string/#connection-string-formats) - uri of mongo database
- database - database name of mongodb
- collection - collection name of mongodb

Examples:

```sql
CREATE BATCH INPUT TABLE raw_log USING mongo(uri="yourMongoUri",database="yourDatabaseName",collection="yourCollectionName")
```
### Output
Syntax

```sql
#batch
CREATE BATCH OUTPUT TABLE table_identifier USING mongo(uri=<mongoUri>, database=<database>,collection=<collection>);

#streaming
CREATE STREAM OUTPUT TABLE table_identifier USING streaming-mongo(uri=<mongoUri>, database=<database>,collection=<collection>) TBLPROPERTIES(checkPointLocation=<checkPointLocation>);

```

Parameters:

- table_identifier -   table name of input table.
- [mongoUri](https://docs.mongodb.com/manual/reference/connection-string/#connection-string-formats) - uri of mongo database
- database - database name of mongodb
- collection - collection name of mongodb

Examples:

```sql
#batch
CREATE BATCH OUTPUT TABLE raw_log USING mongo(uri="yourMongoUri",database="yourDatabaseName",collection="yourCollectionName");

#streaming
create stream output table outputTable using streaming-mongo(
   uri="mongodb://host:port",
   database="test",
   collection="testCol"
 ) TBLPROPERTIES(checkpointLocation="/checkPointDir");

```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-mongo:0.0.4  \
stream-standalone-0.0.4-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


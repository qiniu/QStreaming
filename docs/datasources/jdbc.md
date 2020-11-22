### Input
Syntax

```sql
CREATE BATCH INPUT TABLE table_identifier USING jdbc(url=<jdbcUrl>,user=<jdbcUser>,password=<jdbcPassword>,dbTable=<tableName>,query=<query>,...);
```

Parameters:

- table_identifier -  table name of input table.
- jdbcUrl - jdbc url
- jdbcUser - jdbc user
- jdbcPassword - jdbc password
- tableName - table name to connect
- query - A query that will be used to read data into QStreaming

Please refer to [here](https://spark.apache.org/docs/latest/sql-data-sources-jdbc.html) for more information about the jdbc connector properties

Examples:

```sql
CREATE BATCH INPUT TABLE raw_log USING jdbc(url="jdbcUrl",user="jdbcUser",password="jdbcPassword",query="select * from user_behavior");
```

### Output
Syntax:

```sql
#batch
create batch output table table_identifier USING jdbc(url=<url>, dbTable=<dbTable>, user=<user>, password=<password>, driver=<driver>) TBLPROPERTIES(saveMode=<saveMode>);

#streaming
create stream output table table_identifier USING streaming-jdbc(url=<url>, dbtable=<dbTable>, user=<user>, password=<password>, driver=<driver>) TBLPROPERTIES(outputMode=<saveMode>,checkpointLocation=<checkPointLocation>);
```

Parameters:

- table_identifier -  table name of output table
- url - jdbc url
- dbTable - table name
- user - jdbc user
- password - jdbc password
- driver - class name of jdbc driver
- saveMode (batch used only)
- outputMode(streaming used only)

Examples:

```sql
#batch
create batch output table test USING jdbc(url="jdbc:mysql://localhost/test?", dbTable="table1", user="test" password="password", driver="com.mysql.jdbc.Driver") TBLPROPERTIES(saveMode="overwrite");

#streaming
create stream output table test USING streaming-jdbc(url="jdbc:mysql://localhost/test?", dbtable="table1", user="test" password="password", driver="com.mysql.jdbc.Driver") TBLPROPERTIES(outputMode="update",checkpointLocation="/tmp/spark/checkpoint-jdbc");
```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-jdbc:0.1.0, ${driverDependencies} \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```

where ${driverDependencies} is the database driver dependency, for example if you would like to connect to mysql, your spark-submit should be as follow:

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-jdbc:0.1.0,mysql:mysql-connector-java:jar:6.0.6 \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


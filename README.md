QStreaming is a framework that simplifies writing and executing ETLs on top of [Apache Spark](http://spark.apache.org/).

It is based on a simple sql-like configuration file and runs on any Spark cluster.

### Getting started

To run QStreaming you must first define 2 files.

#####  Job DSL

A job.dsl is a sql file defines the queries of the ETL pipeline

For example a simple  job.dsl (actullay is a sql like file) should be as follow:

```sql
-- DDL for streaming input which connect to a kafka topic
-- this declares five fields based on the JSON data format.In addition, it use the ROWTIME() to declare a virtual column that generate the event time attribute from existing ts field
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'5 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);

-- DDL for streaming output which connect to a kafka topic
create stream output table behavior_cnt_per_hour
using kafka(
  kafka.bootstrap.servers="localhost:9091",
  topic="buy_cnt_per_hour"
)TBLPROPERTIES(
  "update-mode"="update",
  checkpointLocation = "buy_cnt_per_hour_cp"
);

-- CREATE VIEW count the number of "buy" records in each hour window.
create view v_behavior_cnt_per_hour as
SELECT
   window(eventTime, "60 minutes")) as window,
   COUNT(*) as behavior_cnt,behavior
FROM user_behavior
GROUP BY window(eventTime, "60 minutes")),behavior;

--  persist result to kafka
insert into buy_cnt_per_hour
select to_json(struct(cast(window.start as long)* 1000 as hour_of_day, behavior_cnt,behavior)) value
from v_behavior_cnt_per_hour
```

##### Application configuration properties

There are only two config options  currently avaliable.

1. debug
2. job.file

```properties
//indicator whether QStreaming is running with DEBUG mode or not
debug = false
//file name of job.dsl(default is job.dsl)
job.file = job.dsl
```

#### Run QStreaming

There are three options to run QStreaming

##### Run on a yarn cluster

To run on a cluster requires [Apache Spark](https://spark.apache.org/) v2.2+

- get latest JAR file

  There are two options to get QStreaming Jar file

  - clone project and build

  ```bash
  git clone git@github.com:qbox/QStreaming.git \
  cd QStreaming && mvn clean install
  ```

  - download the last released JAR

- Run the following command:

``` bash
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.spark.core.StreamingApp \
--master spark://IP:PORT \
--files "application.conf" \
stream-spark-x.y.z.jar
```

##### Run on a standalone cluster

To run on a standalone cluster you must first [start a spark standalone cluster](https://spark.apache.org/docs/latest/spark-standalone.html) , and then  run the  following  command:

```bash
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.spark.core.StreamingApp \
--master yarn \
--deploy-mode client \
--files "application.conf" \
stream-spark-x.y.z.jar
```

##### Run as a library

It's also possible to use QStreaming inside your own project

QStreaming library requires scala 2.11

To use it add the dependency to your project

- maven

  ```xml
  <dependency>
    <groupId>com.qiniu.stream</groupId>
    <dependency>stream-spark</dependency>
    <version>LATEST VERSION</version>
  </dependency>
  ```

- gradle

  ```groovy
  compile group: 'com.qiniu.stream', name: 'stream-spark', version: 'LATEST VERSION'
  ```

- sbt

        ```scala
"com.qiuniu.stream" % "stream-spark" % "LATEST VERSION"
        ```

### Features

#### DDL Support for streaming process

```sql
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'5 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);
```

Above DDL statement define an input which connect to a kafka topic.

For detail information  please refer to [CreateSourceTableStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L38)  for how to define an input and [CreateSinkTableStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L43) for how to define an output.

#### Watermark support in sql

QStreaming supports watermark which helps a stream processing engine to deal with late data.

There are two ways to use watermark for a stream processing engine

- Adding ***ROWTIME(eventTimeField,delayThreshold)*** as a schema property in a  ddl statement

  ```sql
  create stream input table user_behavior(
    user_id LONG,
    item_id LONG,
    category_id LONG,
    behavior STRING,
    ts TIMESTAMP,
    eventTime as ROWTIME(ts,'5 minutes')
  ) using kafka(
    kafka.bootstrap.servers="localhost:9091",
    startingOffsets=earliest,
    subscribe="user_behavior",
    "group-id"="user_behavior"
  );
  ```

  Above example  means use `eventTime` as event time field  with 5 minutes delay threshold

- Adding *** waterMark("eventTimeField, delayThreshold") *** as a  view property in  a view statement

  ```sql
  create view v_behavior_cnt_per_hour as
  SELECT
     window(eventTime, "60 minutes")) as window,
     COUNT(*) as behavior_cnt,
     behavior
  FROM user_behavior
  GROUP BY
    window(eventTime, "60 minutes")),
    behavior;
  ```

Above  example  define a watermark use `eventTime` field with 1 hour threshold

#### Dynamic user define function

```
-- define UDF named hello
def hello(name:String) = {
   s"hello ${name}"
};

```

QStreaming allow to define a dynamic UDF inside job.dsl, for more detail information please refer to [createFunctionStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L16)

Above example define UDF with a string parameter.

#### Multiple sink for streaming application

```sql
create stream output table output using hbase(
        quorum = '192.168.0.2:2181,192.168.0.3:2181,192.168.0.4:2181',
        tableName = 'buy_cnt_per_hour',
        rowKey = '<hour_of_day>',
        cf = 'cf',
        fields = '[{"qualified":"buy_cnt","value":"behavior_cnt","type":"LongType"}]',
        where = 'behavior="buy"'
    ),hbase(
        quorum = 'jjh714:2181,jjh712:2181,jjh713:2181,jjh710:2181,jjh711:2181',
        tableName = 'order_cnt_per_hour
        rowKey = '<hour_of_day>',
        cf = 'cf',
        fields = '[{"qualified":"order_cnt","value":"behavior_cnt","type":"LongType"}]',
        where = 'behavior="order"'
    ) TBLPROPERTIES (outputMode = update,checkpointLocation = "behavior_output");
```

QStreaming allow you to define multiple output for streaming/batch process engine by leavarage  [foreEachBatch](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#using-foreach-and-foreachbatch) mode (only avaliable in spark>=2.4.0)

Above example will sink the behavior count metric to two hbase table, for more information about how to create multiple sink please refer to [createSinkTableStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L43)

#### Vairable interpolation

```sql
create batch input table raw_log
USING parquet(path="hdfs://cluster1/logs/day=<day>/hour=<hour>");
```

job.dsl file support variable interpolation from command line arguments , this  is  useful  for running QStreaming as a periodic job.

For example you can pass the value for  `theDayThatRunAJob` and `theHourThatRunAJob` from an  [Airflow](http://airflow.apache.org/) DAG

```bash
$SPARK_HOME/bin/spark-submit
--name {{.dir}} \
--class com.qiniu.stream.spark.core.StreamingApp \
--master yarn \
--deploy-mode client \
--num-executors 90 \
--executor-cores 4 \
--executor-memory 5g \
--driver-memory 4g \
--files "application.conf" \
--conf spark.driver.extraClassPath=./ \
--conf spark.executor.extraClassPath=./ \
stream-spark-0.0.1.jar \
-day theDayThatRunAJob
-hour  theHourThatRunAJob\
```

#### Kafka lag monitor

QStreaming allow to monitor the kafka topic offset lag by  adding the ***"group-id"*** connector property in  ddl statement as below

```sql
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'5 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);
```


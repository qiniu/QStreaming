[![Gitter](https://badges.gitter.im/qiniu-streaming/community.svg)](https://gitter.im/qiniu-streaming/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Build Status](https://travis-ci.org/qiniu/QStreaming.svg?branch=master)](https://travis-ci.org/qiniu/QStreaming)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

QStreaming is a framework that simplifies writing and executing ETLs on top of [Apache Spark](http://spark.apache.org/)

It is based on a simple sql-like configuration file and runs on any Spark cluster

## Getting started

To run QStreaming you must first define 2 files.

##### Job DSL

A job.dsl is a sql file defines the queries of the ETL pipeline

For example a simple  job.dsl (actually is a sql like file) should be as follows:

```sql
-- DDL for streaming input which connect to a kafka topic
-- this declares five fields based on the JSON data format.In addition, it use the ROWTIME() to declare a virtual column that generate the event time attribute from existing ts field
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'1 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);

-- DDL for streaming output which connect to a kafka topic
create stream output table behavior_cnt_per_hour
using kafka(
  kafka.bootstrap.servers="localhost:9091",
  topic="behavior_cnt_per_hour"
)TBLPROPERTIES(
  "update-mode"="update",
  checkpointLocation = "behavior_cnt_per_hour"
);

-- CREATE VIEW count the number of "buy" records in each hour window.
create view v_behavior_cnt_per_hour as
SELECT
   window(eventTime, "1 minutes") as window,
   COUNT(*) as behavior_cnt,
   behavior
FROM user_behavior
GROUP BY
  window(eventTime, "1 minutes"),
  behavior;


--  persist result to kafka
insert into behavior_cnt_per_hour
select
   from_unixtime(cast(window.start as LONG)/1000,'yyyy-MM-dd HH:mm') as time,
   behavior_cnt,
   behavior
from
  v_behavior_cnt_per_hour;
```

##### Application configuration properties

There are only two config options  currently avaliable.

1. debug
2. job.file

```properties
debug = false //indicator whether QStreaming is running with DEBUG mode or not
job.file = job.dsl //file name of job.dsl(default is job.dsl)
```

#### Run QStreaming

There are three options to run QStreaming

##### Run on a yarn cluster

To run on a cluster requires [Apache Spark](https://spark.apache.org/) v2.2+

- get the latest JAR file

  There are two options to get QStreaming Jar files

  - clone project and build

  ```bash
  git clone git@github.com:qbox/QStreaming.git \
  cd QStreaming && mvn clean install
  ```

  - Download the last released JAR from [here](https://packagecloud.io/qiniu/release)

- Run the following command:

``` bash
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.spark.core.StreamingApp \
--master yarn \
--deploy-mode client \
--files "application.conf" \
stream-spark-0.0.1.jar
```

##### Run on a standalone cluster

To run on a standalone cluster you must first [start a spark standalone cluster](https://spark.apache.org/docs/latest/spark-standalone.html) , and then  run the  following  command:

```bash
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.spark.core.StreamingApp \
--master spark://IP:PORT \
--files "application.conf" \
stream-spark-x.y.z.jar
```

##### Run as a library

It's also possible to use QStreaming inside your own project

QStreaming library requires scala 2.11

To use it adds the dependency to your project

- maven

  add repository below  

  ~~~xml
  <repositories>
    <repository>
      <id>chaojunz-release</id>
      <url>https://packagecloud.io/qiniu/release/maven2</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
  ~~~

  and dependency as follow 

  ```xml
  <dependency>
    <groupId>com.qiniu.stream</groupId>
    <dependency>stream-spark</dependency>
    <version>0.0.1</version>
  </dependency>
  ```

- gradle

  ```groovy
  repositories {
      maven {
          url "https://packagecloud.io/qiniu/release/maven2"
      }
  }
  ```

  ```groovy
  compile 'com.qiniu.stream:stream-spark-example:0.0.1'
  ```

- sbt

  For SNAPSHOT support (needs SBT 0.13.8 or above), create or append the following to a `project/maven.sbt` file in your project:
  
  ```
  addMavenResolverPlugin
  ```
  
  Then, add this entry anywhere in your `build.sbt` file:
  
  ~~~scala
  resolvers += "qiniu-release" at "https://packagecloud.io/qiniu/release/maven2"
  libraryDependencies += "com.qiniu.stream" % "stream-spark-example" % "0.0.1"
  ~~~

## Datasources

we support following datasource as input:

- [Kafka](http://kafka.apache.org/) (streaming) with `json/regex/csv/avro`   format
- HDFS/S3 with `csv/json/text/parquet/avro`   storage format
- [Jdbc](https://en.wikipedia.org/wiki/Java_Database_Connectivity)(e.g. mysql, sqlserver, oracle)
- MongoDB
- [Apache Hbase](http://hbase.apache.org/)

and following datasources as output:

- [Kafka](http://kafka.apache.org/)
- [elasticsearch](https://www.elastic.co/elasticsearch/)
- [Apache Hbase](http://hbase.apache.org/)
- MongoDB
- [Jdbc](https://en.wikipedia.org/wiki/Java_Database_Connectivity)(e.g. mysql, oracle)
- HDFS/S3 with `csv/json/text/parquet/avro`   storage format

## Features

### DDL Support for streaming process

```sql
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'1 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);
```

Above DDL statement define an input which connect to a kafka topic.

For detail information  please refer to [CreateSourceTableStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L38)  for how to define an input and [CreateSinkTableStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L43) for how to define an output.

### Watermark support in sql

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
    eventTime as ROWTIME(ts,'1 minutes')
  ) using kafka(
    kafka.bootstrap.servers="localhost:9091",
    startingOffsets=earliest,
    subscribe="user_behavior",
    "group-id"="user_behavior"
  );
  ```

  Above example  means use `eventTime` as event time field  with 5 minutes delay thresholds

- Adding   ***waterMark("eventTimeField, delayThreshold")***  as a  view property in  a view statement

  ```sql
  create view v_behavior_cnt_per_hour(waterMark = "eventTime, 1 minutes") as
  SELECT
     window(eventTime, "1 minutes") as window,
     COUNT(*) as behavior_cnt,
     behavior
  FROM user_behavior
  GROUP BY
    window(eventTime, "1 minutes"),
    behavior;
  ```

Above  example  define a watermark use `eventTime` field with 1 minute threshold

### Dynamic user define function

```
-- define UDF named hello
def hello(name:String) = {
   s"hello ${name}"
};

```

QStreaming allow to define a dynamic UDF inside job.dsl, for more detail information please refer to [createFunctionStatement](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4#L16)

Above example define UDF with a string parameter.

### The multiple sink for streaming application

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

### Variable interpolation

```sql
create batch input table raw_log
USING parquet(path="hdfs://cluster1/logs/day=<day>/hour=<hour>");
```

job.dsl file support variable interpolation from command line arguments , this  is  useful  for running QStreaming as a periodic job.

For example, you can pass the value for  `theDayThatRunAJob` and `theHourThatRunAJob` from an  [Airflow](http://airflow.apache.org/) DAG

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

### Kafka lag monitor

QStreaming allow to monitor the kafka topic offset lag by  adding the ***"group-id"*** connector property in  ddl statement as below

```sql
create stream input table user_behavior(
  user_id LONG,
  item_id LONG,
  category_id LONG,
  behavior STRING,
  ts TIMESTAMP,
  eventTime as ROWTIME(ts,'1 minutes')
) using kafka(
  kafka.bootstrap.servers="localhost:9091",
  startingOffsets=earliest,
  subscribe="user_behavior",
  "group-id"="user_behavior"
);
```

## Contributing
We welcome all kinds of contribution, including bug reports, feature requests, documentation improvements, UI refinements, etc.

Thanks to all [contributors](https://github.com/qiniu/QStreaming/graphs/contributors)!!


## Join QStreaming WeChat Group

![image-20200924173745117](docs/image/wechat.png)
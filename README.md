### Introduction

QStreaming is a library that simplifies writing and executing ETLs on top of [Apache Spark](http://spark.apache.org/).

It is based on a simple sql-like configuration file and runs on any Spark cluster.

### Getting started

#### Build project 

```maven
mvn clean install 
```

you will get a jar named "stream-spark-0.0.1.jar " in folder {basedir}/stream-spark/target

#### Run QStreaming 

To run QStreaming you must first define 2 files.

- job.dsl 

job.dsl is a sql file defines the queries of the ETL pipeline

A streaming process example

```sql
-- DDL for streaming input
create stream input table raw_log(name STRING,country STRING,salary INTEGER,eventTime LONG,eventTime as ROWTIME(eventTime,'5 minutes')) using kafka(kafka.bootstrap.servers="localhost:${actualConfig.kafkaPort}",startingOffsets=earliest, subscribe=salary,"group-id"=test);

-- DDL for streaming output
create stream output table salary using kafka( kafka.bootstrap.servers="localhost:9091",topic=test) TBLPROPERTIES("update-mode"="update");

-- origin spark sql
create view salary_view  as select eventTime,country,name,salary  from salary ;

-- orgin spark sql
insert into salary  select to_json(struct( country, avg(salary) avg_salary)) value  from salary_view group by  country;
```

Make sure to also check out the full [Spark SQL Language manual](https://docs.databricks.com/spark/latest/spark-sql/index.html#sql-language-manual) for the possible queries

- application.conf

```properties
debug = false
job.file = job.dsl
```

And then submit as spark job as bellow:

``` bash  
#!/bin/bash
export JAVA_HOME={pointToYourJavaHome}
export HADOOP_HOME={pointToYourHadoopHome}
export SPARK_HOME={pointToYourSparkHome}
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export YARN_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export SPARK_CONF_DIR=${SPARK_HOME}/conf

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
stream-spark-0.0.1.jar
```

please change the above settings according to the corresponding configuration based on your environment



### Features

#### DDL Support for streaming process

```sql 
create stream input table raw_log(name STRING,country STRING,salary INTEGER,eventTime LONG,eventTime as ROWTIME(eventTime,'5 minutes')) using kafka(kafka.bootstrap.servers="localhost:${actualConfig.kafkaPort}",startingOffsets=earliest, subscribe=salary,"group-id"=test);
```

Above DDL statement use kafka as streaming input. for detail information about the full syntax ,please check [sql.g4](https://github.com/qbox/QStreaming/blob/master/stream-spark/src/main/antlr4/com/qiniu/stream/spark/parser/Sql.g4)  

#### Watermark support in sql

watermark can be used inside a  ddl statement or a   view statement.

- Used inside a DDL statement

  ```sql 
  create stream input table raw_log(name STRING,country STRING,salary INTEGER,eventTime LONG,eventTime as ROWTIME(eventTime,'5 minutes')) using kafka(kafka.bootstrap.servers="localhost:${actualConfig.kafkaPort}",startingOffsets=earliest, subscribe=salary,"group-id"=test);
  ```

  For example ***ROWTIME(eventTime,'5 minutes')*** means use `eventTime` as event time field  with 5 minutes sliding window

- Used insde a View statement 

  ```sql
  create view viewName with (waterMark="eventTime, 1 hour") as
  select
      firstName,
      lastName,
      age
  from employee
  ```

For example above statement define a watermark use `eventTime` field with 1 hour slide window

#### Dynamic User Defined Function

```
-- define UDF named hello 
def hello(name) = {
   s"hello ${name}"
};

```

Dynamic User Defined function is same as  scala function definition 

#### Multiple sink for streaming application

```
create stream output table sink_table using 
kafka( kafka.bootstrap.servers="localhost:9091",topic=topic1),
kafka( kafka.bootstrap.servers="localhost:9091",topic=topic2)
TBLPROPERTIES (outputMode = update,checkpointLocation = multiple_sink_checkpoint);
```

You see that above statement define an output table with two kafka sink

#### Vairable render

```sql 
create batch input table raw_log USING parquet(path="hdfs://cluster1/logs/day=<day>/hour=<hour>");
// other sql
create batch output table summary USING parquet(path="hdfs://cluster2/logs/summary/day=<day>/hour=<hour>") TBLPROPERTIES(saveMode=overwrite);
```

This  feature is  useful  for batch process scenario that the batch is schedued and you can pass into  runtime  variable to this sql  file .

You can pass `day` and `hour` variables  as follow

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
-day 20200920 \
-hour  \
```

#### Kafka lag monitor 

due to spark  kafka consumer use low level api instead of high level api with subscribe mode ,so you can not get the lag of  topic.

QStreaming allow to monitor the offset lag by  adding the "group-id" property in your stream ddl statement as below

```sql 
create stream input table raw_log(name STRING,country STRING,salary INTEGER,eventTime LONG,eventTime as ROWTIME(eventTime,'5 minutes')) using kafka(kafka.bootstrap.servers="localhost:${actualConfig.kafkaPort}",startingOffsets=earliest, subscribe=salary,"group-id"=test);
```


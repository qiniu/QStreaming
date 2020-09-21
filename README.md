### Introduction

QStreaming is a library that simplifies writing and executing ETLs on top of [Apache Spark](http://spark.apache.org/).

It is based on a simple sql-like configuration file and runs on any Spark cluster.

### Features

- DDL Support for streaming process
- Watermark support in sql
- Dynamic User Defined Function
- Multiple sink for streaming application
- Vairable render
- Kafka lag monitor

### Getting started

#### Build project

```maven
mvn clean install
```

you will get a jar named "stream-spark-0.0.1.jar " in folder {basedir}/stream-spark/target

#### Run QStreaming

##### Prepare files

To run QStreaming you must first define 2 files.

- job.dsl

job.dsl is a sql file defines the queries of the ETL pipeline

For example

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

##### Run pipeline on yarn

create a bash file named run.sh as follow

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
--num-executors 1 \
--executor-cores 4 \
--executor-memory 2g \
--driver-memory 1g \
--files "application.conf" \
--conf spark.driver.extraClassPath=./ \
--conf spark.executor.extraClassPath=./ \
stream-spark-0.0.1.jar
```

please change the above settings according to the corresponding configuration based on your environment
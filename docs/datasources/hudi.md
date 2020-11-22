### Input

### Output
Syntax：

```sql
create batch output table table_identifier USING hudi(
                                                      hoodie.datasource.write.table.name=<hudiTableName>,
                                                      hoodie.datasource.hive_sync.table=<tableName>,...
                                                     ) TBLPROPERTIES(saveMode=<saveMode>);
```

Parameters:

- table_identifier -  table name of output table
- hudiTableName - table name that will be used for registering with Hive. Needs to be same across runs
- tableName - table to sync to

For more information about the hudi options please refer to [here](https://hudi.apache.org/docs/configurations.html#write-options)

Examples:

```sql
create batch output table test USING hudi(path=<yourHdfsPath>) TBLPROPERTIES(saveMode="overwrite");
```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-hudi:0.1.0  \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


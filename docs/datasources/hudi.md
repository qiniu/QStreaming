### Input

Syntax：

```sql
#batch 
create batch input table table_identifier USING hudi(path=<path>) ; -- load 
```

Parameters:

- table_identifier - table name of input table
- path - partition path where the hudi file hosts

Examples:

```sql
create batch input table hudi_trips_snapshot USING hudi(path="/path/to/your/hudi/location") ;
```

### Output

Syntax：

```sql
#update data
create batch output table table_identifier USING hudi(
  hoodie.datasource.write.precombine.field=<preCombineField>,
  hoodie.datasource.write.recordkey.field=<recordKeyField>,
  hoodie.datasource.write.partitionpath.field=<partitionPathField>,
  hoodie.datasource.write.table.name=<tableName>
) TBLPROPERTIES(saveMode="append");

#insert data
create batch output table table_identifier USING hudi(
  hoodie.datasource.write.precombine.field=<preCombineField>,
  hoodie.datasource.write.recordkey.field=<recordKeyField>,
  hoodie.datasource.write.partitionpath.field=<partitionPathField>,
  hoodie.datasource.write.table.name=<tableName>
) TBLPROPERTIES(saveMode="overwrite");

#delete data
create batch output table table_identifier USING hudi(
  hoodie.datasource.write.operation="delete",
  hoodie.datasource.write.precombine.field=<preCombineField>,
  hoodie.datasource.write.recordkey.field=<recordKeyField>,
  hoodie.datasource.write.partitionpath.field=<partitionPathField>,
  hoodie.datasource.write.table.name=<tableName>
) TBLPROPERTIES(saveMode="append");
```

Parameters:

- table_identifier -  table name of output table
- preCombineField - Field used in preCombining before actual write. When two records have the same key value, we will pick the one with the largest value for the precombine field, determined by Object.compareTo(..)
- recordKeyField - Record key field. Value to be used as the recordKey component of HoodieKey. Actual value will be obtained by invoking .toString() on the field value. Nested fields can be specified using the dot notation eg: a.b.c
- partitionPathField - Partition path field. Value to be used at the partitionPath component of HoodieKey. Actual value ontained by invoking .toString()
- tableName - Hive table name, to register the table into. Default: None (mandatory)

For more information about the hudi options please refer to [here](https://hudi.apache.org/docs/configurations.html#write-options)

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-hudi:0.1.0  \
--conf 'spark.serializer=org.apache.spark.serializer.KryoSerializer' \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 

```


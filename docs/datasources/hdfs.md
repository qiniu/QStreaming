### Input
Syntax

```sql
CREATE BATCH INPUT TABLE table_identifier USING format(path=<a_full_path_in_hdfs_or_s3>);
```

Parameters:

- table_identifier -  table name of input table.
- format - could be (parquet/csv/text/avro)

Examples:

```sql
#parquet
CREATE BATCH INPUT TABLE raw_log USING parquet(path="<yourHdfsFullPath>");
#csv
CREATE BATCH INPUT TABLE raw_log USING csv(path="<yourHdfsFullPath>");
#text
CREATE BATCH INPUT TABLE raw_log USING text(path="<yourHdfsFullPath>");
#avro
CREATE BATCH INPUT TABLE raw_log USING avro(path="<yourHdfsFullPath>");
```

### Output
Syntax：

```sql
#batch
create batch output table table_identifier USING parquet(path=<path>) TBLPROPERTIES(saveMode=<saveMode>);
```

Parameters:

- table_identifier -  table name of output table

- path - hdfs path

- saveMode - saveMode is used to specify the expected behavior of saving a reslt to a data source ( e.g. append|error|ignore|overwrite).

  please refer to [here](https://spark.apache.org/docs/2.2.0/api/java/index.html?org/apache/spark/sql/SaveMode.html) for detail information of saveMode

Examples:

```sql
#parquet
create batch output table test USING parquet(path=<yourHdfsPath>) TBLPROPERTIES(saveMode="overwrite");
#csv
create batch output table test USING csv(path=<yourHdfsPath>) TBLPROPERTIES(saveMode="overwrite");
#avro
create batch output table test USING avro(path=<yourHdfsPath>) TBLPROPERTIES(saveMode="overwrite");
#text
create batch output table test USING text(path=<yourHdfsPath>) TBLPROPERTIES(saveMode="overwrite");
```
create stream input table inputTable using custom(reader="org.apache.spark.sql.elasticsearch.ESWriteTestSource");

create stream output table outputTable using streaming-es(
   nodes="localhost",
   resource="test/test",
   port="${port}"
 ) TBLPROPERTIES(checkpointLocation="${checkPointDir}");

insert into  outputTable SELECT name from  inputTable;
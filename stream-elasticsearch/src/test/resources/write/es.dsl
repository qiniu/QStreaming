create stream input table inputTable using custom(reader="org.apache.spark.sql.elasticsearch.MongoWriteTestSource");

create stream output table outputTable using streaming-es(
   nodes="localhost",
   port="${port}",
   resource="test/test"
 ) TBLPROPERTIES(checkpointLocation="${checkPointDir}");

insert into  outputTable SELECT name from  inputTable;
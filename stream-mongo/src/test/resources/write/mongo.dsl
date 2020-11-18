create stream input table inputTable using custom(reader="org.apache.spark.sql.mongo.MongoWriteTestSource");

create stream output table outputTable using streaming-mongo(
   uri="mongodb://localhost:${port}",
   database="test",
   collection="testCol"
 ) TBLPROPERTIES(checkpointLocation="${checkPointDir}");

insert into  outputTable SELECT name from  inputTable;
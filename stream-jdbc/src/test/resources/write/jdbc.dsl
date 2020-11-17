create stream input table inputTable using custom(reader="org.apache.spark.sql.jdbc.JdbcWriteTestSource");

create stream output table outputTable using streaming-jdbc(
   url="jdbc:h2:mem:testdb",
   dbtable="stream_test_table",
   driver="org.h2.Driver"
 ) TBLPROPERTIES(checkpointLocation="/tmp/checkpoint/behavior_cnt_per_hour");

insert into  outputTable SELECT name from  inputTable;
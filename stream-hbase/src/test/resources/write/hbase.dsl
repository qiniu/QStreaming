create stream input table inputTable using custom(reader="org.apache.spark.sql.hbase.HBaseWriteTestSource");

create stream output table outputTable using streaming-hbase(
  catalog='{
      "table":{
    	"namespace":"default",
    	"name":"test"
      },
      "rowkey":"key1",
      "columns":{
         "name":{"cf":"rowkey", "col":"key1", "type":"string"},
         "value":{"cf":"cf", "col":"value", "type":"int"}
      }
  }', hbase.zookeeper.quorum="${zkQuorum}", hbase.zookeeper.property.clientPort="${zkClientPort}"
 ) TBLPROPERTIES(checkpointLocation="${checkPointDir}");

insert into  outputTable SELECT name,value from  inputTable;
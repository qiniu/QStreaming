### Input
Syntax:

```sql
#batch
CREATE BATCH INPUT TABLE table_identifier USING es(port=<port>,nodes=<nodes>,resource="<index>/<type>");

```

Parameters:

- port - port of es cluster
- nodes - host of es cluster
- index - name of es index
- type - type of es index

Examples:

```sql
#batch
create batch input table dogs using es
(resource='index/dogs',  nodes= 'localhost',port='9999');
```

### Output

Syntax:

```sql
#batch
CREATE BATCH OUTPUT TABLE table_identifier USING es(port=<port>,nodes=<nodes>,
                                                    resource="<index>/<type>");
//streaming
CREATE STREAM OUTPUT TABLE table_identifier USING streaming-es(port=<port>,nodes=<nodes>,resource="<index>/<type>");
```

Parameters:

- port - port of es cluster
- nodes - host of es cluster
- index - name of es index
- type - type of es index

Examples:

```sql
#batch
create batch output table dogs
using es (resource='index/dogs', nodes= 'localhost', port='9999');

#streaming
create stream output table dogs
using streaming-es (resource='index/dogs', nodes= 'localhost',port='9999');
```

### spark-submit

```shell
$SPARK_HOME/bin/spark-submit
--class com.qiniu.stream.core.Streaming \
--master spark://IP:PORT \
--packages com.qiniu:stream-elasticsearch6:0.1.0  \
stream-standalone-0.1.0-jar-with-dependencies.jar \
-j pathToYourPipeline.dsl 
```


### Input
Syntax:

```sql
//QStreaming SQL
CREATE BATCH INPUT TABLE table_identifier USING es(port=<port>,nodes=<nodes>,resource="<index>/<type>");

```

Parameters:

- port - port of es cluster
- nodes - host of es cluster
- index - name of es index
- type - type of es index

Examples:

```sql
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
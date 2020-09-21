
grammar Sql;

sql
    : (dslStatement ';' )+
    ;

dslStatement
    : createSourceTableStatement |createSinkTableStatement |createFunctionStatement | createViewStatement |insertStatement |comment | sqlStatement
    ;

sqlStatement
    :   ~(';' )+
    ;

createFunctionStatement
    : functionDataType? K_CREATE K_FUNCTION funcName=IDENTIFIER ( '(' funcParam  (',' funcParam)* ')')? '=' funcBody= statementBody
    | 'def' funcName=IDENTIFIER ( '(' funcParam  (',' funcParam)* ')')? '=' funcBody=statementBody
    ;

functionDataType
   : '@type(' structField (',' structField)* ')'
   ;

structField
   : STRING ':' fieldType
   ;

statementBody
    : '{' ~(';' )+ '}'
    |  ~(';' )+
    ;

funcParam
    : IDENTIFIER ':' ('String'|'Boolean'|'Int'|'Long'|'Double'|'BigInt'|'BigDecimal')
    ;

createSourceTableStatement
    : K_CREATE streamTable=(K_STREAM| K_BATCH) K_INPUT K_TABLE tableName schemaSpec? K_USING connectorSpec formatSpec?  tableProperties?
    ;


createSinkTableStatement
    : K_CREATE streamTable=(K_STREAM| K_BATCH) K_OUTPUT K_TABLE tableName schemaSpec? K_USING (connectorSpec (',' connectorSpec)* ) formatSpec? partitionSpec? bucketSpec ? tableProperties?
    ;

partitionSpec
    : K_PARTITIONED K_BY '(' columns+=IDENTIFIER (',' columns+=IDENTIFIER)* ')'
    ;

bucketSpec
    : K_CLUSTERED K_BY '(' columns+=IDENTIFIER (',' columns+=IDENTIFIER)* ')' K_INTO bucketNum=INTEGER_VALUE K_BUCKETS
    ;

tableProperties
    :  'TBLPROPERTIES' '(' property (',' property)* ')'
    ;

connectorSpec
    : connectorType=identity ('(' connectProps+=property (','  connectProps+=property)* ')' )?
    ;

schemaSpec
    : '(' schemaField (',' schemaField)* (',' timeField )? ')'
    ;

formatSpec
    : K_ROW K_FORMAT rowFormat ( '(' property (',' property)* ')')?
    ;


timeField
    : fieldName=identity K_AS 'PROCTIME()'                                                    #procTime
    | eventTime=identity K_AS 'ROWTIME(' fromField=identity ',' delayThreadsHold=identity  ')' #rowTime
    ;

rowFormat
    :'TEXT'|'AVRO'|'JSON'|'CSV'|'REGEX'
    ;

schemaField
    : fieldName=identity fieldType
    ;

fieldType
    :'INTEGER'|'LONG'|'TINYINT'|'SMALLINT'|'STRING'|'TIMESTAMP'|'DATE'|'TIME'|'DATETIME'|'BOOLEAN'|'DOUBLE'|'FLOAT'|'SHORT'|'BYTE'|'VARCHAR'|('DECIMAL(' precision=INTEGER_VALUE ','  scale=INTEGER_VALUE ')')
    ;

insertStatement
    : K_INSERT K_INTO tableName  selectStatement
    ;

createViewStatement
    : K_CREATE (K_GLOBAL K_TEMPORARY | K_TEMPORARY | K_PERSISTED)?  K_VIEW tableName (K_WITH '('property ( ',' property )* ')' )? K_AS selectStatement
    ;

selectStatement
    : K_SELECT  ~(';' )+
    ;

property
    : propertyKey=identity K_EQ propertyValue=identity
    ;

identity
    : IDENTIFIER ('.' IDENTIFIER)*
    | STRING
    ;

tableName
    : identity
    ;

//comment goes here
comment
    : SINGLE_LINE_COMMENT| MULTILINE_COMMENT | SQL_LINE_COMMENT
    ;


//keyword goes here
K_CLUSTERED: C L U S T E R E D;
K_GLOBAL: G L O B A L;
K_TEMPORARY: T E M P;
K_PERSISTED: P E R S I S T E D;
K_BY: B Y ;
K_PARTITIONED: P A R T I T I O N E D ;
K_BUCKETS: B U C K E T S ;
K_AS: A S;
K_SELECT: S E L E C T;
K_INTO: I N T O;
K_CREATE: C R E A T E;
K_INSERT: I N S E R T;
K_VIEW: V I E W;
K_TABLE: T A B L E;
K_WITH: W I T H;
K_OPTIONS: O P T I O N S;
K_STREAM: S T R E A M;
K_BATCH: B A T C H;
K_INPUT: I N P U T;
K_OUTPUT: O U T P U T;
K_USING: U S I N G;
K_ROW: R O W;
K_FORMAT: F O R M A T ;
K_EQ: '=';
K_SET: S E T;
K_FUNCTION: F U N C T I O N;

//common lexer goes here
IDENTIFIER
    : [a-zA-Z_0-9]+ [a-zA-Z_0-9]*
    ;

STRING
    : '"'  ( ~'"'  | '""'   )* '"'
    | '\'' ( ~'\'' | '\'\'' )* '\''
    | '`'  ( ~'`'  | '``'   )* '`'
    ;

SINGLE_LINE_COMMENT
    : '//' ~[\r\n]* -> channel(HIDDEN)
    ;

SQL_LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

MULTILINE_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;


WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

// Catch-all for anything we can't recognize.
// We use this to be able to ignore and recover all the text
// when splitting statements with DelimiterLexer
UNRECOGNIZED
    : .
    ;

INTEGER_VALUE
    : DIGIT+
    ;

fragment DIGIT : ('0'..'9');
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
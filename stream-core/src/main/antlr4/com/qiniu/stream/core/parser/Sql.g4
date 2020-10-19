
/*
 * Copyright 2020 Qiniu Cloud (qiniu.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
grammar Sql;


@members {
  /**
   * When false, INTERSECT is given the greater precedence over the other set
   * operations (UNION, EXCEPT and MINUS) as per the SQL standard.
   */
  public boolean legacy_setops_precedence_enbled = false;

  /**
   * Verify whether current token is a valid decimal token (which contains dot).
   * Returns true if the character that follows the token is not a digit or letter or underscore.
   *
   * For example:
   * For char stream "2.3", "2." is not a valid decimal token, because it is followed by digit '3'.
   * For char stream "2.3_", "2.3" is not a valid decimal token, because it is followed by '_'.
   * For char stream "2.3W", "2.3" is not a valid decimal token, because it is followed by 'W'.
   * For char stream "12.0D 34.E2+0.12 "  12.0D is a valid decimal token because it is followed
   * by a space. 34.E2 is a valid decimal token because it is followed by symbol '+'
   * which is not a digit or letter or underscore.
   */
  public boolean isValidDecimal() {
    int nextChar = _input.LA(1);
    if (nextChar >= 'A' && nextChar <= 'Z' || nextChar >= '0' && nextChar <= '9' ||
      nextChar == '_') {
      return false;
    } else {
      return true;
    }
  }
}

sql
    : (dslStatement ';' )+
    ;

dslStatement
    : createTestStatement
    | createSourceTableStatement
    | createSinkTableStatement
    | createFunctionStatement
    | createViewStatement
    | insertStatement
    | commentStatement
    | sqlStatement
    ;

createTestStatement
    : K_CREATE K_TEST testName=identifier ('(' testOptions ')')?  K_ON testDataset=tableIdentifier K_WITH  constraint (K_AND constraint)*
    ;

testOptions
    : 'output' K_EQ testOutput=tableIdentifier
    | 'level' K_EQ testLevel=('Warning'|'Error')
    ;

constraint
    :  'numRows()'     constraintOperator value = INTEGER_VALUE                                                              #sizeConstraint
    |  'isUnique'  '(' column=identifier (',' column=identifier)* ')'                                                        #uniqueConstraint
    |  'hasDistinct''(' column=identifier ')'                                                                                #distinctConstraint
    |  kind=('isAlwaysNull' |'isNotNull') '(' column=identifier ')'                                                          #completeConstraint
    |  'satisfy' '(' predicate=STRING ',' desc=STRING ')'                                                                    #satisfyConstraint
    |  'hasDataType' '(' column=identifier ',' dataType= ('NULL'|'INT'|'BOOL'|'FRACTIONAL'|'TEXT'|'NUMERIC')  ')'            #dataTypeConstraint
    |  kind=('hasMinLength'|'hasMaxLength') '(' column=identifier ',' length=INTEGER_VALUE ')'                               #minMaxLengthConstraint
    |  kind=('hasMin'|'hasMax'|'hasSum'|'hasMean') '(' column=identifier ',' value = DECIMAL_VALUE ')'                       #minMaxValueConstraint
    |  'hasPattern' '('  column=identifier ',' pattern=STRING ')'                                                            #patternConstraint
    |  'hasDateFormat' '(' column=identifier ',' formatString=STRING ')'                                                     #dateFormatConstraint
    |  'isEqualTo' '(' tableName=tableIdentifier ')'                                                                         #exactlyEqualConstraint
    |  'hasForeignKey' '(' referenceTable=tableIdentifier ',' column=identifier ',' referenceColumn=identifier ')'           #foreignKeyConstraint
    |  'hasApproxQuantile'  '(' column=identifier ',' quantile=DECIMAL_VALUE ',' constraintOperator value = DECIMAL_VALUE ')'#approxQuantileConstraint
    |  'hasApproxCountDistinct' '(' column=identifier ','  constraintOperator value = DECIMAL_VALUE ')'                      #approxCountDistinctConstraint
    ;

constraintOperator
    : ('>'|'<'|'>='|'<='|'==' |'!=')
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
    : K_CREATE streamTable=(K_STREAM| K_BATCH) K_INPUT K_TABLE tableIdentifier schemaSpec? K_USING connectorSpec formatSpec? tableProperties?
    ;


createSinkTableStatement
    : K_CREATE streamTable=(K_STREAM| K_BATCH) K_OUTPUT K_TABLE tableIdentifier schemaSpec? K_USING (connectorSpec (',' connectorSpec)* ) formatSpec? partitionSpec? bucketSpec ? tableProperties?
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
    : connectorType=qualifiedName ('(' connectProps+=property (','  connectProps+=property)* ')' )?
    ;

schemaSpec
    : '(' schemaField (',' schemaField)* (',' timeField )? ')'
    ;

formatSpec
    : K_ROW K_FORMAT rowFormat ( '(' property (',' property)* ')')?
    ;


timeField
    : fieldName=identifier K_AS 'PROCTIME()'                                                         #procTime
    | eventTime=identifier K_AS 'ROWTIME(' fromField=identifier ',' delayThreadsHold=identifier  ')' #rowTime
    ;

rowFormat
    :'TEXT'|'AVRO'|'JSON'|'CSV'|'REGEX'
    ;

schemaField
    : fieldName=identifier fieldType (K_COMMENT STRING)?
    ;

fieldType
    :'INTEGER'|'LONG'|'TINYINT'|'SMALLINT'|'STRING'|'TIMESTAMP'|'DATE'|'TIME'|'DATETIME'|'BOOLEAN'|'DOUBLE'|'FLOAT'|'SHORT'|'BYTE'|'VARCHAR'|('DECIMAL(' precision=INTEGER_VALUE ','  scale=INTEGER_VALUE ')')
    ;

insertStatement
    : K_INSERT K_INTO tableIdentifier  selectStatement
    ;

createViewStatement
    : K_CREATE (K_OR K_REPLACE)? (K_GLOBAL ? K_TEMPORARY )?  K_VIEW viewName=tableIdentifier (K_WITH '('property ( ',' property )* ')' )? K_AS selectStatement
    ;

selectStatement
    : K_SELECT  ~(';' )+
    ;

property
    : propertyKey K_EQ propertyValue
    ;

propertyKey
    : identifier ('.' identifier)*
    | STRING
    ;

propertyValue
    : INTEGER_VALUE
    | DECIMAL_VALUE
    | booleanValue
    | STRING
    ;

qualifiedName
    : identifier ('.' identifier)*
    ;

identifier
    : strictIdentifier
    ;

strictIdentifier
    : IDENTIFIER             #unquotedIdentifier
    | quotedIdentifier       #quotedIdentifierAlternative
    ;

quotedIdentifier
    : BACKQUOTED_IDENTIFIER
    ;

tableIdentifier
    : (db=identifier '.')? table=identifier
    ;

booleanValue
    : ('true' |'TRUE') | ('false' | 'FALSE')
    ;


//comment goes here
commentStatement
    : SIMPLE_COMMENT| BRACKETED_COMMENT | BRACKETED_EMPTY_COMMENT
    ;

sqlStatement
    :   ~(';' )+
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
K_COMMENT: C O M M E N T ;
K_TEST: T E S T;
K_CHECK: C H E C K;
K_OR: O R;
K_REPLACE: R E P L A C E;
K_AND: A N D;
K_ON: O N;

STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    ;

BIGINT_LITERAL
    : DIGIT+ 'L'
    ;

SMALLINT_LITERAL
    : DIGIT+ 'S'
    ;

TINYINT_LITERAL
    : DIGIT+ 'Y'
    ;

INTEGER_VALUE
    : DIGIT+
    ;

DECIMAL_VALUE
    : DIGIT+ EXPONENT
    | DECIMAL_DIGITS EXPONENT? {isValidDecimal()}?
    ;

IDENTIFIER
    : (LETTER | DIGIT | '_')+
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

fragment DECIMAL_DIGITS
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

fragment EXPONENT
    : 'E' [+-]? DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [a-zA-Z]
    ;

//a-z case insensitive
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

SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_EMPTY_COMMENT
    : '/**/' -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' ~[+] .*? '*/' -> channel(HIDDEN)
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
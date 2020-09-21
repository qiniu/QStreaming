// Generated from com/qiniu/stream/core/parser/Sql.g4 by ANTLR 4.8
package com.qiniu.stream.core.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, K_CLUSTERED=42, K_GLOBAL=43, K_TEMPORARY=44, 
		K_PERSISTED=45, K_BY=46, K_PARTITIONED=47, K_BUCKETS=48, K_AS=49, K_SELECT=50, 
		K_INTO=51, K_CREATE=52, K_INSERT=53, K_VIEW=54, K_TABLE=55, K_WITH=56, 
		K_OPTIONS=57, K_STREAM=58, K_BATCH=59, K_INPUT=60, K_OUTPUT=61, K_USING=62, 
		K_ROW=63, K_FORMAT=64, K_EQ=65, K_SET=66, K_FUNCTION=67, IDENTIFIER=68, 
		STRING=69, SINGLE_LINE_COMMENT=70, SQL_LINE_COMMENT=71, MULTILINE_COMMENT=72, 
		WS=73, UNRECOGNIZED=74, INTEGER_VALUE=75;
	public static final int
		RULE_sql = 0, RULE_dslStatement = 1, RULE_sqlStatement = 2, RULE_createFunctionStatement = 3, 
		RULE_functionDataType = 4, RULE_structField = 5, RULE_statementBody = 6, 
		RULE_funcParam = 7, RULE_createSourceTableStatement = 8, RULE_createSinkTableStatement = 9, 
		RULE_partitionSpec = 10, RULE_bucketSpec = 11, RULE_tableProperties = 12, 
		RULE_connectorSpec = 13, RULE_schemaSpec = 14, RULE_formatSpec = 15, RULE_timeField = 16, 
		RULE_rowFormat = 17, RULE_schemaField = 18, RULE_fieldType = 19, RULE_insertStatement = 20, 
		RULE_createViewStatement = 21, RULE_selectStatement = 22, RULE_property = 23, 
		RULE_identity = 24, RULE_tableName = 25, RULE_comment = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"sql", "dslStatement", "sqlStatement", "createFunctionStatement", "functionDataType", 
			"structField", "statementBody", "funcParam", "createSourceTableStatement", 
			"createSinkTableStatement", "partitionSpec", "bucketSpec", "tableProperties", 
			"connectorSpec", "schemaSpec", "formatSpec", "timeField", "rowFormat", 
			"schemaField", "fieldType", "insertStatement", "createViewStatement", 
			"selectStatement", "property", "identity", "tableName", "comment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "','", "')'", "'def'", "'@type('", "':'", "'{'", 
			"'}'", "'String'", "'Boolean'", "'Int'", "'Long'", "'Double'", "'BigInt'", 
			"'BigDecimal'", "'TBLPROPERTIES'", "'PROCTIME()'", "'ROWTIME('", "'TEXT'", 
			"'AVRO'", "'JSON'", "'CSV'", "'REGEX'", "'INTEGER'", "'LONG'", "'TINYINT'", 
			"'SMALLINT'", "'STRING'", "'TIMESTAMP'", "'DATE'", "'TIME'", "'DATETIME'", 
			"'BOOLEAN'", "'DOUBLE'", "'FLOAT'", "'SHORT'", "'BYTE'", "'VARCHAR'", 
			"'DECIMAL('", "'.'", null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "K_CLUSTERED", "K_GLOBAL", "K_TEMPORARY", 
			"K_PERSISTED", "K_BY", "K_PARTITIONED", "K_BUCKETS", "K_AS", "K_SELECT", 
			"K_INTO", "K_CREATE", "K_INSERT", "K_VIEW", "K_TABLE", "K_WITH", "K_OPTIONS", 
			"K_STREAM", "K_BATCH", "K_INPUT", "K_OUTPUT", "K_USING", "K_ROW", "K_FORMAT", 
			"K_EQ", "K_SET", "K_FUNCTION", "IDENTIFIER", "STRING", "SINGLE_LINE_COMMENT", 
			"SQL_LINE_COMMENT", "MULTILINE_COMMENT", "WS", "UNRECOGNIZED", "INTEGER_VALUE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Sql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class SqlContext extends ParserRuleContext {
		public List<DslStatementContext> dslStatement() {
			return getRuleContexts(DslStatementContext.class);
		}
		public DslStatementContext dslStatement(int i) {
			return getRuleContext(DslStatementContext.class,i);
		}
		public SqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlContext sql() throws RecognitionException {
		SqlContext _localctx = new SqlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_sql);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(54);
				dslStatement();
				setState(55);
				match(T__0);
				}
				}
				setState(59); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << K_CLUSTERED) | (1L << K_GLOBAL) | (1L << K_TEMPORARY) | (1L << K_PERSISTED) | (1L << K_BY) | (1L << K_PARTITIONED) | (1L << K_BUCKETS) | (1L << K_AS) | (1L << K_SELECT) | (1L << K_INTO) | (1L << K_CREATE) | (1L << K_INSERT) | (1L << K_VIEW) | (1L << K_TABLE) | (1L << K_WITH) | (1L << K_OPTIONS) | (1L << K_STREAM) | (1L << K_BATCH) | (1L << K_INPUT) | (1L << K_OUTPUT) | (1L << K_USING) | (1L << K_ROW))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (K_FORMAT - 64)) | (1L << (K_EQ - 64)) | (1L << (K_SET - 64)) | (1L << (K_FUNCTION - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (STRING - 64)) | (1L << (SINGLE_LINE_COMMENT - 64)) | (1L << (SQL_LINE_COMMENT - 64)) | (1L << (MULTILINE_COMMENT - 64)) | (1L << (WS - 64)) | (1L << (UNRECOGNIZED - 64)) | (1L << (INTEGER_VALUE - 64)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DslStatementContext extends ParserRuleContext {
		public CreateSourceTableStatementContext createSourceTableStatement() {
			return getRuleContext(CreateSourceTableStatementContext.class,0);
		}
		public CreateSinkTableStatementContext createSinkTableStatement() {
			return getRuleContext(CreateSinkTableStatementContext.class,0);
		}
		public CreateFunctionStatementContext createFunctionStatement() {
			return getRuleContext(CreateFunctionStatementContext.class,0);
		}
		public CreateViewStatementContext createViewStatement() {
			return getRuleContext(CreateViewStatementContext.class,0);
		}
		public InsertStatementContext insertStatement() {
			return getRuleContext(InsertStatementContext.class,0);
		}
		public CommentContext comment() {
			return getRuleContext(CommentContext.class,0);
		}
		public SqlStatementContext sqlStatement() {
			return getRuleContext(SqlStatementContext.class,0);
		}
		public DslStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dslStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterDslStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitDslStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitDslStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DslStatementContext dslStatement() throws RecognitionException {
		DslStatementContext _localctx = new DslStatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_dslStatement);
		try {
			setState(68);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(61);
				createSourceTableStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(62);
				createSinkTableStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(63);
				createFunctionStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(64);
				createViewStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(65);
				insertStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(66);
				comment();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(67);
				sqlStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SqlStatementContext extends ParserRuleContext {
		public SqlStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSqlStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSqlStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSqlStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlStatementContext sqlStatement() throws RecognitionException {
		SqlStatementContext _localctx = new SqlStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_sqlStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(70);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==T__0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(73); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << K_CLUSTERED) | (1L << K_GLOBAL) | (1L << K_TEMPORARY) | (1L << K_PERSISTED) | (1L << K_BY) | (1L << K_PARTITIONED) | (1L << K_BUCKETS) | (1L << K_AS) | (1L << K_SELECT) | (1L << K_INTO) | (1L << K_CREATE) | (1L << K_INSERT) | (1L << K_VIEW) | (1L << K_TABLE) | (1L << K_WITH) | (1L << K_OPTIONS) | (1L << K_STREAM) | (1L << K_BATCH) | (1L << K_INPUT) | (1L << K_OUTPUT) | (1L << K_USING) | (1L << K_ROW))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (K_FORMAT - 64)) | (1L << (K_EQ - 64)) | (1L << (K_SET - 64)) | (1L << (K_FUNCTION - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (STRING - 64)) | (1L << (SINGLE_LINE_COMMENT - 64)) | (1L << (SQL_LINE_COMMENT - 64)) | (1L << (MULTILINE_COMMENT - 64)) | (1L << (WS - 64)) | (1L << (UNRECOGNIZED - 64)) | (1L << (INTEGER_VALUE - 64)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreateFunctionStatementContext extends ParserRuleContext {
		public Token funcName;
		public StatementBodyContext funcBody;
		public TerminalNode K_CREATE() { return getToken(SqlParser.K_CREATE, 0); }
		public TerminalNode K_FUNCTION() { return getToken(SqlParser.K_FUNCTION, 0); }
		public TerminalNode K_EQ() { return getToken(SqlParser.K_EQ, 0); }
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public StatementBodyContext statementBody() {
			return getRuleContext(StatementBodyContext.class,0);
		}
		public FunctionDataTypeContext functionDataType() {
			return getRuleContext(FunctionDataTypeContext.class,0);
		}
		public List<FuncParamContext> funcParam() {
			return getRuleContexts(FuncParamContext.class);
		}
		public FuncParamContext funcParam(int i) {
			return getRuleContext(FuncParamContext.class,i);
		}
		public CreateFunctionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createFunctionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCreateFunctionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCreateFunctionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitCreateFunctionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateFunctionStatementContext createFunctionStatement() throws RecognitionException {
		CreateFunctionStatementContext _localctx = new CreateFunctionStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_createFunctionStatement);
		int _la;
		try {
			setState(113);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
			case K_CREATE:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(75);
					functionDataType();
					}
				}

				setState(78);
				match(K_CREATE);
				setState(79);
				match(K_FUNCTION);
				setState(80);
				((CreateFunctionStatementContext)_localctx).funcName = match(IDENTIFIER);
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(81);
					match(T__1);
					setState(82);
					funcParam();
					setState(87);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(83);
						match(T__2);
						setState(84);
						funcParam();
						}
						}
						setState(89);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(90);
					match(T__3);
					}
				}

				setState(94);
				match(K_EQ);
				setState(95);
				((CreateFunctionStatementContext)_localctx).funcBody = statementBody();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				match(T__4);
				setState(97);
				((CreateFunctionStatementContext)_localctx).funcName = match(IDENTIFIER);
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(98);
					match(T__1);
					setState(99);
					funcParam();
					setState(104);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__2) {
						{
						{
						setState(100);
						match(T__2);
						setState(101);
						funcParam();
						}
						}
						setState(106);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(107);
					match(T__3);
					}
				}

				setState(111);
				match(K_EQ);
				setState(112);
				((CreateFunctionStatementContext)_localctx).funcBody = statementBody();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDataTypeContext extends ParserRuleContext {
		public List<StructFieldContext> structField() {
			return getRuleContexts(StructFieldContext.class);
		}
		public StructFieldContext structField(int i) {
			return getRuleContext(StructFieldContext.class,i);
		}
		public FunctionDataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterFunctionDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitFunctionDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitFunctionDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDataTypeContext functionDataType() throws RecognitionException {
		FunctionDataTypeContext _localctx = new FunctionDataTypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_functionDataType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(T__5);
			setState(116);
			structField();
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(117);
				match(T__2);
				setState(118);
				structField();
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructFieldContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SqlParser.STRING, 0); }
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public StructFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterStructField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitStructField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitStructField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructFieldContext structField() throws RecognitionException {
		StructFieldContext _localctx = new StructFieldContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_structField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(STRING);
			setState(127);
			match(T__6);
			setState(128);
			fieldType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementBodyContext extends ParserRuleContext {
		public StatementBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterStatementBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitStatementBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitStatementBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementBodyContext statementBody() throws RecognitionException {
		StatementBodyContext _localctx = new StatementBodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_statementBody);
		int _la;
		try {
			int _alt;
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				match(T__7);
				setState(132); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(131);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__0) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(134); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(136);
				match(T__8);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(138); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(137);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__0) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(140); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << K_CLUSTERED) | (1L << K_GLOBAL) | (1L << K_TEMPORARY) | (1L << K_PERSISTED) | (1L << K_BY) | (1L << K_PARTITIONED) | (1L << K_BUCKETS) | (1L << K_AS) | (1L << K_SELECT) | (1L << K_INTO) | (1L << K_CREATE) | (1L << K_INSERT) | (1L << K_VIEW) | (1L << K_TABLE) | (1L << K_WITH) | (1L << K_OPTIONS) | (1L << K_STREAM) | (1L << K_BATCH) | (1L << K_INPUT) | (1L << K_OUTPUT) | (1L << K_USING) | (1L << K_ROW))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (K_FORMAT - 64)) | (1L << (K_EQ - 64)) | (1L << (K_SET - 64)) | (1L << (K_FUNCTION - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (STRING - 64)) | (1L << (SINGLE_LINE_COMMENT - 64)) | (1L << (SQL_LINE_COMMENT - 64)) | (1L << (MULTILINE_COMMENT - 64)) | (1L << (WS - 64)) | (1L << (UNRECOGNIZED - 64)) | (1L << (INTEGER_VALUE - 64)))) != 0) );
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncParamContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public FuncParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterFuncParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitFuncParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitFuncParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncParamContext funcParam() throws RecognitionException {
		FuncParamContext _localctx = new FuncParamContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_funcParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(IDENTIFIER);
			setState(145);
			match(T__6);
			setState(146);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreateSourceTableStatementContext extends ParserRuleContext {
		public Token streamTable;
		public TerminalNode K_CREATE() { return getToken(SqlParser.K_CREATE, 0); }
		public TerminalNode K_INPUT() { return getToken(SqlParser.K_INPUT, 0); }
		public TerminalNode K_TABLE() { return getToken(SqlParser.K_TABLE, 0); }
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public TerminalNode K_USING() { return getToken(SqlParser.K_USING, 0); }
		public ConnectorSpecContext connectorSpec() {
			return getRuleContext(ConnectorSpecContext.class,0);
		}
		public TerminalNode K_STREAM() { return getToken(SqlParser.K_STREAM, 0); }
		public TerminalNode K_BATCH() { return getToken(SqlParser.K_BATCH, 0); }
		public SchemaSpecContext schemaSpec() {
			return getRuleContext(SchemaSpecContext.class,0);
		}
		public FormatSpecContext formatSpec() {
			return getRuleContext(FormatSpecContext.class,0);
		}
		public TablePropertiesContext tableProperties() {
			return getRuleContext(TablePropertiesContext.class,0);
		}
		public CreateSourceTableStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createSourceTableStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCreateSourceTableStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCreateSourceTableStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitCreateSourceTableStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateSourceTableStatementContext createSourceTableStatement() throws RecognitionException {
		CreateSourceTableStatementContext _localctx = new CreateSourceTableStatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_createSourceTableStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(K_CREATE);
			setState(149);
			((CreateSourceTableStatementContext)_localctx).streamTable = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==K_STREAM || _la==K_BATCH) ) {
				((CreateSourceTableStatementContext)_localctx).streamTable = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(150);
			match(K_INPUT);
			setState(151);
			match(K_TABLE);
			setState(152);
			tableName();
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(153);
				schemaSpec();
				}
			}

			setState(156);
			match(K_USING);
			setState(157);
			connectorSpec();
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ROW) {
				{
				setState(158);
				formatSpec();
				}
			}

			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(161);
				tableProperties();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreateSinkTableStatementContext extends ParserRuleContext {
		public Token streamTable;
		public TerminalNode K_CREATE() { return getToken(SqlParser.K_CREATE, 0); }
		public TerminalNode K_OUTPUT() { return getToken(SqlParser.K_OUTPUT, 0); }
		public TerminalNode K_TABLE() { return getToken(SqlParser.K_TABLE, 0); }
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public TerminalNode K_USING() { return getToken(SqlParser.K_USING, 0); }
		public TerminalNode K_STREAM() { return getToken(SqlParser.K_STREAM, 0); }
		public TerminalNode K_BATCH() { return getToken(SqlParser.K_BATCH, 0); }
		public List<ConnectorSpecContext> connectorSpec() {
			return getRuleContexts(ConnectorSpecContext.class);
		}
		public ConnectorSpecContext connectorSpec(int i) {
			return getRuleContext(ConnectorSpecContext.class,i);
		}
		public SchemaSpecContext schemaSpec() {
			return getRuleContext(SchemaSpecContext.class,0);
		}
		public FormatSpecContext formatSpec() {
			return getRuleContext(FormatSpecContext.class,0);
		}
		public PartitionSpecContext partitionSpec() {
			return getRuleContext(PartitionSpecContext.class,0);
		}
		public BucketSpecContext bucketSpec() {
			return getRuleContext(BucketSpecContext.class,0);
		}
		public TablePropertiesContext tableProperties() {
			return getRuleContext(TablePropertiesContext.class,0);
		}
		public CreateSinkTableStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createSinkTableStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCreateSinkTableStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCreateSinkTableStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitCreateSinkTableStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateSinkTableStatementContext createSinkTableStatement() throws RecognitionException {
		CreateSinkTableStatementContext _localctx = new CreateSinkTableStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_createSinkTableStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(K_CREATE);
			setState(165);
			((CreateSinkTableStatementContext)_localctx).streamTable = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==K_STREAM || _la==K_BATCH) ) {
				((CreateSinkTableStatementContext)_localctx).streamTable = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(166);
			match(K_OUTPUT);
			setState(167);
			match(K_TABLE);
			setState(168);
			tableName();
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(169);
				schemaSpec();
				}
			}

			setState(172);
			match(K_USING);
			{
			setState(173);
			connectorSpec();
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(174);
				match(T__2);
				setState(175);
				connectorSpec();
				}
				}
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ROW) {
				{
				setState(181);
				formatSpec();
				}
			}

			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_PARTITIONED) {
				{
				setState(184);
				partitionSpec();
				}
			}

			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_CLUSTERED) {
				{
				setState(187);
				bucketSpec();
				}
			}

			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__16) {
				{
				setState(190);
				tableProperties();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartitionSpecContext extends ParserRuleContext {
		public Token IDENTIFIER;
		public List<Token> columns = new ArrayList<Token>();
		public TerminalNode K_PARTITIONED() { return getToken(SqlParser.K_PARTITIONED, 0); }
		public TerminalNode K_BY() { return getToken(SqlParser.K_BY, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(SqlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SqlParser.IDENTIFIER, i);
		}
		public PartitionSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partitionSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterPartitionSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitPartitionSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitPartitionSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartitionSpecContext partitionSpec() throws RecognitionException {
		PartitionSpecContext _localctx = new PartitionSpecContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_partitionSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(K_PARTITIONED);
			setState(194);
			match(K_BY);
			setState(195);
			match(T__1);
			setState(196);
			((PartitionSpecContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			((PartitionSpecContext)_localctx).columns.add(((PartitionSpecContext)_localctx).IDENTIFIER);
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(197);
				match(T__2);
				setState(198);
				((PartitionSpecContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				((PartitionSpecContext)_localctx).columns.add(((PartitionSpecContext)_localctx).IDENTIFIER);
				}
				}
				setState(203);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(204);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BucketSpecContext extends ParserRuleContext {
		public Token IDENTIFIER;
		public List<Token> columns = new ArrayList<Token>();
		public Token bucketNum;
		public TerminalNode K_CLUSTERED() { return getToken(SqlParser.K_CLUSTERED, 0); }
		public TerminalNode K_BY() { return getToken(SqlParser.K_BY, 0); }
		public TerminalNode K_INTO() { return getToken(SqlParser.K_INTO, 0); }
		public TerminalNode K_BUCKETS() { return getToken(SqlParser.K_BUCKETS, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(SqlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SqlParser.IDENTIFIER, i);
		}
		public TerminalNode INTEGER_VALUE() { return getToken(SqlParser.INTEGER_VALUE, 0); }
		public BucketSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bucketSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterBucketSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitBucketSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitBucketSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BucketSpecContext bucketSpec() throws RecognitionException {
		BucketSpecContext _localctx = new BucketSpecContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_bucketSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(K_CLUSTERED);
			setState(207);
			match(K_BY);
			setState(208);
			match(T__1);
			setState(209);
			((BucketSpecContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			((BucketSpecContext)_localctx).columns.add(((BucketSpecContext)_localctx).IDENTIFIER);
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(210);
				match(T__2);
				setState(211);
				((BucketSpecContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				((BucketSpecContext)_localctx).columns.add(((BucketSpecContext)_localctx).IDENTIFIER);
				}
				}
				setState(216);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(217);
			match(T__3);
			setState(218);
			match(K_INTO);
			setState(219);
			((BucketSpecContext)_localctx).bucketNum = match(INTEGER_VALUE);
			setState(220);
			match(K_BUCKETS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TablePropertiesContext extends ParserRuleContext {
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public TablePropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableProperties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTableProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTableProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitTableProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TablePropertiesContext tableProperties() throws RecognitionException {
		TablePropertiesContext _localctx = new TablePropertiesContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_tableProperties);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(T__16);
			setState(223);
			match(T__1);
			setState(224);
			property();
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(225);
				match(T__2);
				setState(226);
				property();
				}
				}
				setState(231);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(232);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConnectorSpecContext extends ParserRuleContext {
		public IdentityContext connectorType;
		public PropertyContext property;
		public List<PropertyContext> connectProps = new ArrayList<PropertyContext>();
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public ConnectorSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectorSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterConnectorSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitConnectorSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitConnectorSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectorSpecContext connectorSpec() throws RecognitionException {
		ConnectorSpecContext _localctx = new ConnectorSpecContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_connectorSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			((ConnectorSpecContext)_localctx).connectorType = identity();
			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(235);
				match(T__1);
				setState(236);
				((ConnectorSpecContext)_localctx).property = property();
				((ConnectorSpecContext)_localctx).connectProps.add(((ConnectorSpecContext)_localctx).property);
				setState(241);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(237);
					match(T__2);
					setState(238);
					((ConnectorSpecContext)_localctx).property = property();
					((ConnectorSpecContext)_localctx).connectProps.add(((ConnectorSpecContext)_localctx).property);
					}
					}
					setState(243);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(244);
				match(T__3);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaSpecContext extends ParserRuleContext {
		public List<SchemaFieldContext> schemaField() {
			return getRuleContexts(SchemaFieldContext.class);
		}
		public SchemaFieldContext schemaField(int i) {
			return getRuleContext(SchemaFieldContext.class,i);
		}
		public TimeFieldContext timeField() {
			return getRuleContext(TimeFieldContext.class,0);
		}
		public SchemaSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSchemaSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSchemaSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSchemaSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaSpecContext schemaSpec() throws RecognitionException {
		SchemaSpecContext _localctx = new SchemaSpecContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_schemaSpec);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(T__1);
			setState(249);
			schemaField();
			setState(254);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(250);
					match(T__2);
					setState(251);
					schemaField();
					}
					} 
				}
				setState(256);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			}
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(257);
				match(T__2);
				setState(258);
				timeField();
				}
			}

			setState(261);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormatSpecContext extends ParserRuleContext {
		public TerminalNode K_ROW() { return getToken(SqlParser.K_ROW, 0); }
		public TerminalNode K_FORMAT() { return getToken(SqlParser.K_FORMAT, 0); }
		public RowFormatContext rowFormat() {
			return getRuleContext(RowFormatContext.class,0);
		}
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public FormatSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formatSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterFormatSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitFormatSpec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitFormatSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormatSpecContext formatSpec() throws RecognitionException {
		FormatSpecContext _localctx = new FormatSpecContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_formatSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(K_ROW);
			setState(264);
			match(K_FORMAT);
			setState(265);
			rowFormat();
			setState(277);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(266);
				match(T__1);
				setState(267);
				property();
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(268);
					match(T__2);
					setState(269);
					property();
					}
					}
					setState(274);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(275);
				match(T__3);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeFieldContext extends ParserRuleContext {
		public TimeFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timeField; }
	 
		public TimeFieldContext() { }
		public void copyFrom(TimeFieldContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class RowTimeContext extends TimeFieldContext {
		public IdentityContext eventTime;
		public IdentityContext fromField;
		public IdentityContext delayThreadsHold;
		public TerminalNode K_AS() { return getToken(SqlParser.K_AS, 0); }
		public List<IdentityContext> identity() {
			return getRuleContexts(IdentityContext.class);
		}
		public IdentityContext identity(int i) {
			return getRuleContext(IdentityContext.class,i);
		}
		public RowTimeContext(TimeFieldContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterRowTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitRowTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitRowTime(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProcTimeContext extends TimeFieldContext {
		public IdentityContext fieldName;
		public TerminalNode K_AS() { return getToken(SqlParser.K_AS, 0); }
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public ProcTimeContext(TimeFieldContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterProcTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitProcTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitProcTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeFieldContext timeField() throws RecognitionException {
		TimeFieldContext _localctx = new TimeFieldContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_timeField);
		try {
			setState(291);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				_localctx = new ProcTimeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(279);
				((ProcTimeContext)_localctx).fieldName = identity();
				setState(280);
				match(K_AS);
				setState(281);
				match(T__17);
				}
				break;
			case 2:
				_localctx = new RowTimeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(283);
				((RowTimeContext)_localctx).eventTime = identity();
				setState(284);
				match(K_AS);
				setState(285);
				match(T__18);
				setState(286);
				((RowTimeContext)_localctx).fromField = identity();
				setState(287);
				match(T__2);
				setState(288);
				((RowTimeContext)_localctx).delayThreadsHold = identity();
				setState(289);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RowFormatContext extends ParserRuleContext {
		public RowFormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rowFormat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterRowFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitRowFormat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitRowFormat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RowFormatContext rowFormat() throws RecognitionException {
		RowFormatContext _localctx = new RowFormatContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_rowFormat);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaFieldContext extends ParserRuleContext {
		public IdentityContext fieldName;
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public SchemaFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSchemaField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSchemaField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSchemaField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaFieldContext schemaField() throws RecognitionException {
		SchemaFieldContext _localctx = new SchemaFieldContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_schemaField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			((SchemaFieldContext)_localctx).fieldName = identity();
			setState(296);
			fieldType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldTypeContext extends ParserRuleContext {
		public Token precision;
		public Token scale;
		public List<TerminalNode> INTEGER_VALUE() { return getTokens(SqlParser.INTEGER_VALUE); }
		public TerminalNode INTEGER_VALUE(int i) {
			return getToken(SqlParser.INTEGER_VALUE, i);
		}
		public FieldTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterFieldType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitFieldType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitFieldType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldTypeContext fieldType() throws RecognitionException {
		FieldTypeContext _localctx = new FieldTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_fieldType);
		try {
			setState(318);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				enterOuterAlt(_localctx, 1);
				{
				setState(298);
				match(T__24);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(299);
				match(T__25);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 3);
				{
				setState(300);
				match(T__26);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 4);
				{
				setState(301);
				match(T__27);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 5);
				{
				setState(302);
				match(T__28);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 6);
				{
				setState(303);
				match(T__29);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 7);
				{
				setState(304);
				match(T__30);
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 8);
				{
				setState(305);
				match(T__31);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 9);
				{
				setState(306);
				match(T__32);
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 10);
				{
				setState(307);
				match(T__33);
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 11);
				{
				setState(308);
				match(T__34);
				}
				break;
			case T__35:
				enterOuterAlt(_localctx, 12);
				{
				setState(309);
				match(T__35);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 13);
				{
				setState(310);
				match(T__36);
				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 14);
				{
				setState(311);
				match(T__37);
				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 15);
				{
				setState(312);
				match(T__38);
				}
				break;
			case T__39:
				enterOuterAlt(_localctx, 16);
				{
				{
				setState(313);
				match(T__39);
				setState(314);
				((FieldTypeContext)_localctx).precision = match(INTEGER_VALUE);
				setState(315);
				match(T__2);
				setState(316);
				((FieldTypeContext)_localctx).scale = match(INTEGER_VALUE);
				setState(317);
				match(T__3);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InsertStatementContext extends ParserRuleContext {
		public TerminalNode K_INSERT() { return getToken(SqlParser.K_INSERT, 0); }
		public TerminalNode K_INTO() { return getToken(SqlParser.K_INTO, 0); }
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public SelectStatementContext selectStatement() {
			return getRuleContext(SelectStatementContext.class,0);
		}
		public InsertStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterInsertStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitInsertStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitInsertStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertStatementContext insertStatement() throws RecognitionException {
		InsertStatementContext _localctx = new InsertStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_insertStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			match(K_INSERT);
			setState(321);
			match(K_INTO);
			setState(322);
			tableName();
			setState(323);
			selectStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreateViewStatementContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SqlParser.K_CREATE, 0); }
		public TerminalNode K_VIEW() { return getToken(SqlParser.K_VIEW, 0); }
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(SqlParser.K_AS, 0); }
		public SelectStatementContext selectStatement() {
			return getRuleContext(SelectStatementContext.class,0);
		}
		public TerminalNode K_GLOBAL() { return getToken(SqlParser.K_GLOBAL, 0); }
		public TerminalNode K_TEMPORARY() { return getToken(SqlParser.K_TEMPORARY, 0); }
		public TerminalNode K_PERSISTED() { return getToken(SqlParser.K_PERSISTED, 0); }
		public TerminalNode K_WITH() { return getToken(SqlParser.K_WITH, 0); }
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public CreateViewStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createViewStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCreateViewStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCreateViewStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitCreateViewStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateViewStatementContext createViewStatement() throws RecognitionException {
		CreateViewStatementContext _localctx = new CreateViewStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_createViewStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
			match(K_CREATE);
			setState(330);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_GLOBAL:
				{
				setState(326);
				match(K_GLOBAL);
				setState(327);
				match(K_TEMPORARY);
				}
				break;
			case K_TEMPORARY:
				{
				setState(328);
				match(K_TEMPORARY);
				}
				break;
			case K_PERSISTED:
				{
				setState(329);
				match(K_PERSISTED);
				}
				break;
			case K_VIEW:
				break;
			default:
				break;
			}
			setState(332);
			match(K_VIEW);
			setState(333);
			tableName();
			setState(346);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(334);
				match(K_WITH);
				setState(335);
				match(T__1);
				setState(336);
				property();
				setState(341);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(337);
					match(T__2);
					setState(338);
					property();
					}
					}
					setState(343);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(344);
				match(T__3);
				}
			}

			setState(348);
			match(K_AS);
			setState(349);
			selectStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectStatementContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(SqlParser.K_SELECT, 0); }
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSelectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSelectStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSelectStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_selectStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(351);
			match(K_SELECT);
			setState(353); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(352);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==T__0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(355); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << K_CLUSTERED) | (1L << K_GLOBAL) | (1L << K_TEMPORARY) | (1L << K_PERSISTED) | (1L << K_BY) | (1L << K_PARTITIONED) | (1L << K_BUCKETS) | (1L << K_AS) | (1L << K_SELECT) | (1L << K_INTO) | (1L << K_CREATE) | (1L << K_INSERT) | (1L << K_VIEW) | (1L << K_TABLE) | (1L << K_WITH) | (1L << K_OPTIONS) | (1L << K_STREAM) | (1L << K_BATCH) | (1L << K_INPUT) | (1L << K_OUTPUT) | (1L << K_USING) | (1L << K_ROW))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (K_FORMAT - 64)) | (1L << (K_EQ - 64)) | (1L << (K_SET - 64)) | (1L << (K_FUNCTION - 64)) | (1L << (IDENTIFIER - 64)) | (1L << (STRING - 64)) | (1L << (SINGLE_LINE_COMMENT - 64)) | (1L << (SQL_LINE_COMMENT - 64)) | (1L << (MULTILINE_COMMENT - 64)) | (1L << (WS - 64)) | (1L << (UNRECOGNIZED - 64)) | (1L << (INTEGER_VALUE - 64)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public IdentityContext propertyKey;
		public IdentityContext propertyValue;
		public TerminalNode K_EQ() { return getToken(SqlParser.K_EQ, 0); }
		public List<IdentityContext> identity() {
			return getRuleContexts(IdentityContext.class);
		}
		public IdentityContext identity(int i) {
			return getRuleContext(IdentityContext.class,i);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_property);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			((PropertyContext)_localctx).propertyKey = identity();
			setState(358);
			match(K_EQ);
			setState(359);
			((PropertyContext)_localctx).propertyValue = identity();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentityContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(SqlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SqlParser.IDENTIFIER, i);
		}
		public TerminalNode STRING() { return getToken(SqlParser.STRING, 0); }
		public IdentityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterIdentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitIdentity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitIdentity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentityContext identity() throws RecognitionException {
		IdentityContext _localctx = new IdentityContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_identity);
		int _la;
		try {
			setState(370);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(361);
				match(IDENTIFIER);
				setState(366);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__40) {
					{
					{
					setState(362);
					match(T__40);
					setState(363);
					match(IDENTIFIER);
					}
					}
					setState(368);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(369);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TableNameContext extends ParserRuleContext {
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitTableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			identity();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode SINGLE_LINE_COMMENT() { return getToken(SqlParser.SINGLE_LINE_COMMENT, 0); }
		public TerminalNode MULTILINE_COMMENT() { return getToken(SqlParser.MULTILINE_COMMENT, 0); }
		public TerminalNode SQL_LINE_COMMENT() { return getToken(SqlParser.SQL_LINE_COMMENT, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_comment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			_la = _input.LA(1);
			if ( !(((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (SINGLE_LINE_COMMENT - 70)) | (1L << (SQL_LINE_COMMENT - 70)) | (1L << (MULTILINE_COMMENT - 70)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3M\u017b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\6\2<\n\2\r\2\16\2=\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3G\n\3\3\4\6\4J\n\4\r\4\16\4K\3\5\5\5O\n\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\7\5X\n\5\f\5\16\5[\13\5\3\5\3\5\5\5_\n\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5i\n\5\f\5\16\5l\13\5\3\5\3\5\5\5p\n\5"+
		"\3\5\3\5\5\5t\n\5\3\6\3\6\3\6\3\6\7\6z\n\6\f\6\16\6}\13\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\7\3\b\3\b\6\b\u0087\n\b\r\b\16\b\u0088\3\b\3\b\6\b\u008d\n"+
		"\b\r\b\16\b\u008e\5\b\u0091\n\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\5\n\u009d\n\n\3\n\3\n\3\n\5\n\u00a2\n\n\3\n\5\n\u00a5\n\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u00ad\n\13\3\13\3\13\3\13\3\13\7\13\u00b3\n"+
		"\13\f\13\16\13\u00b6\13\13\3\13\5\13\u00b9\n\13\3\13\5\13\u00bc\n\13\3"+
		"\13\5\13\u00bf\n\13\3\13\5\13\u00c2\n\13\3\f\3\f\3\f\3\f\3\f\3\f\7\f\u00ca"+
		"\n\f\f\f\16\f\u00cd\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u00d7\n\r"+
		"\f\r\16\r\u00da\13\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\7\16"+
		"\u00e6\n\16\f\16\16\16\u00e9\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\7\17\u00f2\n\17\f\17\16\17\u00f5\13\17\3\17\3\17\5\17\u00f9\n\17\3\20"+
		"\3\20\3\20\3\20\7\20\u00ff\n\20\f\20\16\20\u0102\13\20\3\20\3\20\5\20"+
		"\u0106\n\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\7\21\u0111\n"+
		"\21\f\21\16\21\u0114\13\21\3\21\3\21\5\21\u0118\n\21\3\22\3\22\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0126\n\22\3\23\3\23"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0141\n\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\5\27\u014d\n\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\7\27\u0156\n\27\f\27\16\27\u0159\13\27\3\27\3\27"+
		"\5\27\u015d\n\27\3\27\3\27\3\27\3\30\3\30\6\30\u0164\n\30\r\30\16\30\u0165"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\32\7\32\u016f\n\32\f\32\16\32\u0172\13"+
		"\32\3\32\5\32\u0175\n\32\3\33\3\33\3\34\3\34\3\34\2\2\35\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2\7\3\2\3\3\3\2\f\22\3\2"+
		"<=\3\2\26\32\3\2HJ\2\u019b\2;\3\2\2\2\4F\3\2\2\2\6I\3\2\2\2\bs\3\2\2\2"+
		"\nu\3\2\2\2\f\u0080\3\2\2\2\16\u0090\3\2\2\2\20\u0092\3\2\2\2\22\u0096"+
		"\3\2\2\2\24\u00a6\3\2\2\2\26\u00c3\3\2\2\2\30\u00d0\3\2\2\2\32\u00e0\3"+
		"\2\2\2\34\u00ec\3\2\2\2\36\u00fa\3\2\2\2 \u0109\3\2\2\2\"\u0125\3\2\2"+
		"\2$\u0127\3\2\2\2&\u0129\3\2\2\2(\u0140\3\2\2\2*\u0142\3\2\2\2,\u0147"+
		"\3\2\2\2.\u0161\3\2\2\2\60\u0167\3\2\2\2\62\u0174\3\2\2\2\64\u0176\3\2"+
		"\2\2\66\u0178\3\2\2\289\5\4\3\29:\7\3\2\2:<\3\2\2\2;8\3\2\2\2<=\3\2\2"+
		"\2=;\3\2\2\2=>\3\2\2\2>\3\3\2\2\2?G\5\22\n\2@G\5\24\13\2AG\5\b\5\2BG\5"+
		",\27\2CG\5*\26\2DG\5\66\34\2EG\5\6\4\2F?\3\2\2\2F@\3\2\2\2FA\3\2\2\2F"+
		"B\3\2\2\2FC\3\2\2\2FD\3\2\2\2FE\3\2\2\2G\5\3\2\2\2HJ\n\2\2\2IH\3\2\2\2"+
		"JK\3\2\2\2KI\3\2\2\2KL\3\2\2\2L\7\3\2\2\2MO\5\n\6\2NM\3\2\2\2NO\3\2\2"+
		"\2OP\3\2\2\2PQ\7\66\2\2QR\7E\2\2R^\7F\2\2ST\7\4\2\2TY\5\20\t\2UV\7\5\2"+
		"\2VX\5\20\t\2WU\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[Y\3\2"+
		"\2\2\\]\7\6\2\2]_\3\2\2\2^S\3\2\2\2^_\3\2\2\2_`\3\2\2\2`a\7C\2\2at\5\16"+
		"\b\2bc\7\7\2\2co\7F\2\2de\7\4\2\2ej\5\20\t\2fg\7\5\2\2gi\5\20\t\2hf\3"+
		"\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2km\3\2\2\2lj\3\2\2\2mn\7\6\2\2np\3"+
		"\2\2\2od\3\2\2\2op\3\2\2\2pq\3\2\2\2qr\7C\2\2rt\5\16\b\2sN\3\2\2\2sb\3"+
		"\2\2\2t\t\3\2\2\2uv\7\b\2\2v{\5\f\7\2wx\7\5\2\2xz\5\f\7\2yw\3\2\2\2z}"+
		"\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}{\3\2\2\2~\177\7\6\2\2\177\13\3"+
		"\2\2\2\u0080\u0081\7G\2\2\u0081\u0082\7\t\2\2\u0082\u0083\5(\25\2\u0083"+
		"\r\3\2\2\2\u0084\u0086\7\n\2\2\u0085\u0087\n\2\2\2\u0086\u0085\3\2\2\2"+
		"\u0087\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a"+
		"\3\2\2\2\u008a\u0091\7\13\2\2\u008b\u008d\n\2\2\2\u008c\u008b\3\2\2\2"+
		"\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0091"+
		"\3\2\2\2\u0090\u0084\3\2\2\2\u0090\u008c\3\2\2\2\u0091\17\3\2\2\2\u0092"+
		"\u0093\7F\2\2\u0093\u0094\7\t\2\2\u0094\u0095\t\3\2\2\u0095\21\3\2\2\2"+
		"\u0096\u0097\7\66\2\2\u0097\u0098\t\4\2\2\u0098\u0099\7>\2\2\u0099\u009a"+
		"\79\2\2\u009a\u009c\5\64\33\2\u009b\u009d\5\36\20\2\u009c\u009b\3\2\2"+
		"\2\u009c\u009d\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\7@\2\2\u009f\u00a1"+
		"\5\34\17\2\u00a0\u00a2\5 \21\2\u00a1\u00a0\3\2\2\2\u00a1\u00a2\3\2\2\2"+
		"\u00a2\u00a4\3\2\2\2\u00a3\u00a5\5\32\16\2\u00a4\u00a3\3\2\2\2\u00a4\u00a5"+
		"\3\2\2\2\u00a5\23\3\2\2\2\u00a6\u00a7\7\66\2\2\u00a7\u00a8\t\4\2\2\u00a8"+
		"\u00a9\7?\2\2\u00a9\u00aa\79\2\2\u00aa\u00ac\5\64\33\2\u00ab\u00ad\5\36"+
		"\20\2\u00ac\u00ab\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae"+
		"\u00af\7@\2\2\u00af\u00b4\5\34\17\2\u00b0\u00b1\7\5\2\2\u00b1\u00b3\5"+
		"\34\17\2\u00b2\u00b0\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b5\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\u00b9\5 "+
		"\21\2\u00b8\u00b7\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba"+
		"\u00bc\5\26\f\2\u00bb\u00ba\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00be\3"+
		"\2\2\2\u00bd\u00bf\5\30\r\2\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf"+
		"\u00c1\3\2\2\2\u00c0\u00c2\5\32\16\2\u00c1\u00c0\3\2\2\2\u00c1\u00c2\3"+
		"\2\2\2\u00c2\25\3\2\2\2\u00c3\u00c4\7\61\2\2\u00c4\u00c5\7\60\2\2\u00c5"+
		"\u00c6\7\4\2\2\u00c6\u00cb\7F\2\2\u00c7\u00c8\7\5\2\2\u00c8\u00ca\7F\2"+
		"\2\u00c9\u00c7\3\2\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00cc"+
		"\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00cf\7\6\2\2\u00cf"+
		"\27\3\2\2\2\u00d0\u00d1\7,\2\2\u00d1\u00d2\7\60\2\2\u00d2\u00d3\7\4\2"+
		"\2\u00d3\u00d8\7F\2\2\u00d4\u00d5\7\5\2\2\u00d5\u00d7\7F\2\2\u00d6\u00d4"+
		"\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"\u00db\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\7\6\2\2\u00dc\u00dd\7\65"+
		"\2\2\u00dd\u00de\7M\2\2\u00de\u00df\7\62\2\2\u00df\31\3\2\2\2\u00e0\u00e1"+
		"\7\23\2\2\u00e1\u00e2\7\4\2\2\u00e2\u00e7\5\60\31\2\u00e3\u00e4\7\5\2"+
		"\2\u00e4\u00e6\5\60\31\2\u00e5\u00e3\3\2\2\2\u00e6\u00e9\3\2\2\2\u00e7"+
		"\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ea\3\2\2\2\u00e9\u00e7\3\2"+
		"\2\2\u00ea\u00eb\7\6\2\2\u00eb\33\3\2\2\2\u00ec\u00f8\5\62\32\2\u00ed"+
		"\u00ee\7\4\2\2\u00ee\u00f3\5\60\31\2\u00ef\u00f0\7\5\2\2\u00f0\u00f2\5"+
		"\60\31\2\u00f1\u00ef\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3"+
		"\u00f4\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f7\7\6"+
		"\2\2\u00f7\u00f9\3\2\2\2\u00f8\u00ed\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9"+
		"\35\3\2\2\2\u00fa\u00fb\7\4\2\2\u00fb\u0100\5&\24\2\u00fc\u00fd\7\5\2"+
		"\2\u00fd\u00ff\5&\24\2\u00fe\u00fc\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u00fe"+
		"\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0105\3\2\2\2\u0102\u0100\3\2\2\2\u0103"+
		"\u0104\7\5\2\2\u0104\u0106\5\"\22\2\u0105\u0103\3\2\2\2\u0105\u0106\3"+
		"\2\2\2\u0106\u0107\3\2\2\2\u0107\u0108\7\6\2\2\u0108\37\3\2\2\2\u0109"+
		"\u010a\7A\2\2\u010a\u010b\7B\2\2\u010b\u0117\5$\23\2\u010c\u010d\7\4\2"+
		"\2\u010d\u0112\5\60\31\2\u010e\u010f\7\5\2\2\u010f\u0111\5\60\31\2\u0110"+
		"\u010e\3\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2"+
		"\2\2\u0113\u0115\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0116\7\6\2\2\u0116"+
		"\u0118\3\2\2\2\u0117\u010c\3\2\2\2\u0117\u0118\3\2\2\2\u0118!\3\2\2\2"+
		"\u0119\u011a\5\62\32\2\u011a\u011b\7\63\2\2\u011b\u011c\7\24\2\2\u011c"+
		"\u0126\3\2\2\2\u011d\u011e\5\62\32\2\u011e\u011f\7\63\2\2\u011f\u0120"+
		"\7\25\2\2\u0120\u0121\5\62\32\2\u0121\u0122\7\5\2\2\u0122\u0123\5\62\32"+
		"\2\u0123\u0124\7\6\2\2\u0124\u0126\3\2\2\2\u0125\u0119\3\2\2\2\u0125\u011d"+
		"\3\2\2\2\u0126#\3\2\2\2\u0127\u0128\t\5\2\2\u0128%\3\2\2\2\u0129\u012a"+
		"\5\62\32\2\u012a\u012b\5(\25\2\u012b\'\3\2\2\2\u012c\u0141\7\33\2\2\u012d"+
		"\u0141\7\34\2\2\u012e\u0141\7\35\2\2\u012f\u0141\7\36\2\2\u0130\u0141"+
		"\7\37\2\2\u0131\u0141\7 \2\2\u0132\u0141\7!\2\2\u0133\u0141\7\"\2\2\u0134"+
		"\u0141\7#\2\2\u0135\u0141\7$\2\2\u0136\u0141\7%\2\2\u0137\u0141\7&\2\2"+
		"\u0138\u0141\7\'\2\2\u0139\u0141\7(\2\2\u013a\u0141\7)\2\2\u013b\u013c"+
		"\7*\2\2\u013c\u013d\7M\2\2\u013d\u013e\7\5\2\2\u013e\u013f\7M\2\2\u013f"+
		"\u0141\7\6\2\2\u0140\u012c\3\2\2\2\u0140\u012d\3\2\2\2\u0140\u012e\3\2"+
		"\2\2\u0140\u012f\3\2\2\2\u0140\u0130\3\2\2\2\u0140\u0131\3\2\2\2\u0140"+
		"\u0132\3\2\2\2\u0140\u0133\3\2\2\2\u0140\u0134\3\2\2\2\u0140\u0135\3\2"+
		"\2\2\u0140\u0136\3\2\2\2\u0140\u0137\3\2\2\2\u0140\u0138\3\2\2\2\u0140"+
		"\u0139\3\2\2\2\u0140\u013a\3\2\2\2\u0140\u013b\3\2\2\2\u0141)\3\2\2\2"+
		"\u0142\u0143\7\67\2\2\u0143\u0144\7\65\2\2\u0144\u0145\5\64\33\2\u0145"+
		"\u0146\5.\30\2\u0146+\3\2\2\2\u0147\u014c\7\66\2\2\u0148\u0149\7-\2\2"+
		"\u0149\u014d\7.\2\2\u014a\u014d\7.\2\2\u014b\u014d\7/\2\2\u014c\u0148"+
		"\3\2\2\2\u014c\u014a\3\2\2\2\u014c\u014b\3\2\2\2\u014c\u014d\3\2\2\2\u014d"+
		"\u014e\3\2\2\2\u014e\u014f\78\2\2\u014f\u015c\5\64\33\2\u0150\u0151\7"+
		":\2\2\u0151\u0152\7\4\2\2\u0152\u0157\5\60\31\2\u0153\u0154\7\5\2\2\u0154"+
		"\u0156\5\60\31\2\u0155\u0153\3\2\2\2\u0156\u0159\3\2\2\2\u0157\u0155\3"+
		"\2\2\2\u0157\u0158\3\2\2\2\u0158\u015a\3\2\2\2\u0159\u0157\3\2\2\2\u015a"+
		"\u015b\7\6\2\2\u015b\u015d\3\2\2\2\u015c\u0150\3\2\2\2\u015c\u015d\3\2"+
		"\2\2\u015d\u015e\3\2\2\2\u015e\u015f\7\63\2\2\u015f\u0160\5.\30\2\u0160"+
		"-\3\2\2\2\u0161\u0163\7\64\2\2\u0162\u0164\n\2\2\2\u0163\u0162\3\2\2\2"+
		"\u0164\u0165\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166/\3"+
		"\2\2\2\u0167\u0168\5\62\32\2\u0168\u0169\7C\2\2\u0169\u016a\5\62\32\2"+
		"\u016a\61\3\2\2\2\u016b\u0170\7F\2\2\u016c\u016d\7+\2\2\u016d\u016f\7"+
		"F\2\2\u016e\u016c\3\2\2\2\u016f\u0172\3\2\2\2\u0170\u016e\3\2\2\2\u0170"+
		"\u0171\3\2\2\2\u0171\u0175\3\2\2\2\u0172\u0170\3\2\2\2\u0173\u0175\7G"+
		"\2\2\u0174\u016b\3\2\2\2\u0174\u0173\3\2\2\2\u0175\63\3\2\2\2\u0176\u0177"+
		"\5\62\32\2\u0177\65\3\2\2\2\u0178\u0179\t\6\2\2\u0179\67\3\2\2\2)=FKN"+
		"Y^jos{\u0088\u008e\u0090\u009c\u00a1\u00a4\u00ac\u00b4\u00b8\u00bb\u00be"+
		"\u00c1\u00cb\u00d8\u00e7\u00f3\u00f8\u0100\u0105\u0112\u0117\u0125\u0140"+
		"\u014c\u0157\u015c\u0165\u0170\u0174";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
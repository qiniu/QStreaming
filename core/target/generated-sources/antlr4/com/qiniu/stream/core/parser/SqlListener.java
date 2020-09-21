// Generated from com/qiniu/stream/core/parser/Sql.g4 by ANTLR 4.8
package com.qiniu.stream.core.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlParser}.
 */
public interface SqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlParser#sql}.
	 * @param ctx the parse tree
	 */
	void enterSql(SqlParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#sql}.
	 * @param ctx the parse tree
	 */
	void exitSql(SqlParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#dslStatement}.
	 * @param ctx the parse tree
	 */
	void enterDslStatement(SqlParser.DslStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#dslStatement}.
	 * @param ctx the parse tree
	 */
	void exitDslStatement(SqlParser.DslStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void enterSqlStatement(SqlParser.SqlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 */
	void exitSqlStatement(SqlParser.SqlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#createFunctionStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunctionStatement(SqlParser.CreateFunctionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#createFunctionStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunctionStatement(SqlParser.CreateFunctionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#functionDataType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDataType(SqlParser.FunctionDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#functionDataType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDataType(SqlParser.FunctionDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#structField}.
	 * @param ctx the parse tree
	 */
	void enterStructField(SqlParser.StructFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#structField}.
	 * @param ctx the parse tree
	 */
	void exitStructField(SqlParser.StructFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#statementBody}.
	 * @param ctx the parse tree
	 */
	void enterStatementBody(SqlParser.StatementBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#statementBody}.
	 * @param ctx the parse tree
	 */
	void exitStatementBody(SqlParser.StatementBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#funcParam}.
	 * @param ctx the parse tree
	 */
	void enterFuncParam(SqlParser.FuncParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#funcParam}.
	 * @param ctx the parse tree
	 */
	void exitFuncParam(SqlParser.FuncParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#createSourceTableStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateSourceTableStatement(SqlParser.CreateSourceTableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#createSourceTableStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateSourceTableStatement(SqlParser.CreateSourceTableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#createSinkTableStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateSinkTableStatement(SqlParser.CreateSinkTableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#createSinkTableStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateSinkTableStatement(SqlParser.CreateSinkTableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void enterPartitionSpec(SqlParser.PartitionSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#partitionSpec}.
	 * @param ctx the parse tree
	 */
	void exitPartitionSpec(SqlParser.PartitionSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#bucketSpec}.
	 * @param ctx the parse tree
	 */
	void enterBucketSpec(SqlParser.BucketSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#bucketSpec}.
	 * @param ctx the parse tree
	 */
	void exitBucketSpec(SqlParser.BucketSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#tableProperties}.
	 * @param ctx the parse tree
	 */
	void enterTableProperties(SqlParser.TablePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#tableProperties}.
	 * @param ctx the parse tree
	 */
	void exitTableProperties(SqlParser.TablePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#connectorSpec}.
	 * @param ctx the parse tree
	 */
	void enterConnectorSpec(SqlParser.ConnectorSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#connectorSpec}.
	 * @param ctx the parse tree
	 */
	void exitConnectorSpec(SqlParser.ConnectorSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#schemaSpec}.
	 * @param ctx the parse tree
	 */
	void enterSchemaSpec(SqlParser.SchemaSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#schemaSpec}.
	 * @param ctx the parse tree
	 */
	void exitSchemaSpec(SqlParser.SchemaSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#formatSpec}.
	 * @param ctx the parse tree
	 */
	void enterFormatSpec(SqlParser.FormatSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#formatSpec}.
	 * @param ctx the parse tree
	 */
	void exitFormatSpec(SqlParser.FormatSpecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code procTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 */
	void enterProcTime(SqlParser.ProcTimeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code procTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 */
	void exitProcTime(SqlParser.ProcTimeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rowTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 */
	void enterRowTime(SqlParser.RowTimeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rowTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 */
	void exitRowTime(SqlParser.RowTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void enterRowFormat(SqlParser.RowFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#rowFormat}.
	 * @param ctx the parse tree
	 */
	void exitRowFormat(SqlParser.RowFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#schemaField}.
	 * @param ctx the parse tree
	 */
	void enterSchemaField(SqlParser.SchemaFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#schemaField}.
	 * @param ctx the parse tree
	 */
	void exitSchemaField(SqlParser.SchemaFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void enterFieldType(SqlParser.FieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void exitFieldType(SqlParser.FieldTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(SqlParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(SqlParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#createViewStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateViewStatement(SqlParser.CreateViewStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#createViewStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateViewStatement(SqlParser.CreateViewStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(SqlParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(SqlParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(SqlParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(SqlParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#identity}.
	 * @param ctx the parse tree
	 */
	void enterIdentity(SqlParser.IdentityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#identity}.
	 * @param ctx the parse tree
	 */
	void exitIdentity(SqlParser.IdentityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SqlParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SqlParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(SqlParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(SqlParser.CommentContext ctx);
}
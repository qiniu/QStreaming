// Generated from com/qiniu/stream/core/parser/Sql.g4 by ANTLR 4.8
package com.qiniu.stream.core.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlParser#sql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql(SqlParser.SqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#dslStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDslStatement(SqlParser.DslStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#sqlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlStatement(SqlParser.SqlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#createFunctionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateFunctionStatement(SqlParser.CreateFunctionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#functionDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDataType(SqlParser.FunctionDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#structField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructField(SqlParser.StructFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#statementBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBody(SqlParser.StatementBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#funcParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncParam(SqlParser.FuncParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#createSourceTableStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSourceTableStatement(SqlParser.CreateSourceTableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#createSinkTableStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSinkTableStatement(SqlParser.CreateSinkTableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#partitionSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionSpec(SqlParser.PartitionSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#bucketSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBucketSpec(SqlParser.BucketSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#tableProperties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableProperties(SqlParser.TablePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#connectorSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorSpec(SqlParser.ConnectorSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#schemaSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaSpec(SqlParser.SchemaSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#formatSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatSpec(SqlParser.FormatSpecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code procTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcTime(SqlParser.ProcTimeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rowTime}
	 * labeled alternative in {@link SqlParser#timeField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowTime(SqlParser.RowTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#rowFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowFormat(SqlParser.RowFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#schemaField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaField(SqlParser.SchemaFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#fieldType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldType(SqlParser.FieldTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#insertStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStatement(SqlParser.InsertStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#createViewStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateViewStatement(SqlParser.CreateViewStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStatement(SqlParser.SelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(SqlParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#identity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity(SqlParser.IdentityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SqlParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(SqlParser.CommentContext ctx);
}
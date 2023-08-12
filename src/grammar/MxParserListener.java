// Generated from C:/Users/xun_y/IdeaProjects/Mx-Compilier/src/grammar\MxParser.g4 by ANTLR 4.12.0
package src.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#fileAnalyze}.
	 * @param ctx the parse tree
	 */
	void enterFileAnalyze(MxParser.FileAnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#fileAnalyze}.
	 * @param ctx the parse tree
	 */
	void exitFileAnalyze(MxParser.FileAnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#returnTypename}.
	 * @param ctx the parse tree
	 */
	void enterReturnTypename(MxParser.ReturnTypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#returnTypename}.
	 * @param ctx the parse tree
	 */
	void exitReturnTypename(MxParser.ReturnTypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(MxParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(MxParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#construction}.
	 * @param ctx the parse tree
	 */
	void enterConstruction(MxParser.ConstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#construction}.
	 * @param ctx the parse tree
	 */
	void exitConstruction(MxParser.ConstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(MxParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(MxParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#typename}.
	 * @param ctx the parse tree
	 */
	void enterTypename(MxParser.TypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#typename}.
	 * @param ctx the parse tree
	 */
	void exitTypename(MxParser.TypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#basicVariable}.
	 * @param ctx the parse tree
	 */
	void enterBasicVariable(MxParser.BasicVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#basicVariable}.
	 * @param ctx the parse tree
	 */
	void exitBasicVariable(MxParser.BasicVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(MxParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(MxParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MxParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MxParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(MxParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(MxParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(MxParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(MxParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forVar}.
	 * @param ctx the parse tree
	 */
	void enterForVar(MxParser.ForVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forVar}.
	 * @param ctx the parse tree
	 */
	void exitForVar(MxParser.ForVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(MxParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(MxParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(MxParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(MxParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(MxParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(MxParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(MxParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(MxParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code leftEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLeftEx(MxParser.LeftExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code leftEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLeftEx(MxParser.LeftExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code objEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterObjEx(MxParser.ObjExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code objEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitObjEx(MxParser.ObjExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parent}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParent(MxParser.ParentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parent}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParent(MxParser.ParentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayEx(MxParser.ArrayExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayEx(MxParser.ArrayExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunEx(MxParser.FunExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunEx(MxParser.FunExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryEx(MxParser.BinaryExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryEx(MxParser.BinaryExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryEx(MxParser.UnaryExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryEx(MxParser.UnaryExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssEx(MxParser.AssExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssEx(MxParser.AssExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewEx(MxParser.NewExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewEx(MxParser.NewExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code basicEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBasicEx(MxParser.BasicExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code basicEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBasicEx(MxParser.BasicExContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ternaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryEx(MxParser.TernaryExContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ternaryEx}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryEx(MxParser.TernaryExContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#newArrayExpression}.
	 * @param ctx the parse tree
	 */
	void enterNewArrayExpression(MxParser.NewArrayExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#newArrayExpression}.
	 * @param ctx the parse tree
	 */
	void exitNewArrayExpression(MxParser.NewArrayExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(MxParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(MxParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void enterBasicExpression(MxParser.BasicExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void exitBasicExpression(MxParser.BasicExpressionContext ctx);
}
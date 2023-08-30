package src.ast;

import src.grammar.MxParser;
import src.grammar.MxParserBaseVisitor;
import src.ast.astnode.AstNode;
import src.ast.astnode.FileAnalyze;
import src.ast.astnode.ParameterNode;
import src.ast.astnode.TypeNode;
import src.ast.astnode.definition.*;
import src.ast.astnode.expression.*;
import src.ast.astnode.statement.*;
import src.semantic.eError;

public class AstBuilder extends MxParserBaseVisitor<AstNode> {
    @Override
    public AstNode visitArrayEx(MxParser.ArrayExContext ctx) {
        var exp = new ArrayEx(new Position(ctx), (ExpressionNode) visit(ctx.expression(0)), (ExpressionNode) visit(ctx.expression(1)));
        exp.ss = ctx.getText();
        return exp;
    }

    @Override
    public AstNode visitAssEx(MxParser.AssExContext ctx) {
        return new AssignEx(new Position(ctx), (ExpressionNode) visit(ctx.expression(0)), (ExpressionNode) visit(ctx.expression(1)));
    }

    @Override
    public AstNode visitBasicEx(MxParser.BasicExContext ctx) {
        var exp = (ExpressionNode) visitChildren(ctx);
        exp.ss = ctx.getText();
        return exp;
    }

    @Override
    public AstNode visitBasicExpression(MxParser.BasicExpressionContext ctx) {
        if (ctx.Identifier() == null) {
            return new BasicEx(new Position(ctx), ctx.getText());
        } else {
            return new VarEx(new Position(ctx), ctx.getText());
        }
    }

    @Override
    public AstNode visitBinaryEx(MxParser.BinaryExContext ctx) {
        return new BinaryEx(new Position(ctx), (ExpressionNode) visit(ctx.expression(0)), (ExpressionNode) visit(ctx.expression(1)), ctx.op.getText());
    }

    @Override
    public AstNode visitBlockStatement(MxParser.BlockStatementContext ctx) {
        BlockStatement blo = new BlockStatement(new Position(ctx));
        ctx.statement().forEach(sss -> blo.state.add((Statements) visit(sss)));
        return blo;
    }

    @Override
    public AstNode visitBreakStatement(MxParser.BreakStatementContext ctx) {
        return new Break(new Position(ctx));
    }

    @Override
    public AstNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        ClassDefinition cla = new ClassDefinition(new Position(ctx), ctx.Identifier().getText());
        boolean Con = false;
        for (var decl : ctx.children) {
            if (decl instanceof MxParser.FunctionDeclarationContext) {
                cla.func.add((FunctionDefinition) visit(decl));
            } else if (decl instanceof MxParser.VariableDeclarationContext) {
                cla.var.add((VariableDeclaration) visit(decl));
            } else if (decl instanceof MxParser.ConstructionContext) {
                if (Con) {
                    throw new eError(new Position(ctx), "Class " + cla.name + " has two construction");
                } else {
                    Con = true;
                    cla.con = (Construction) visit(decl);
                }
            }
        }
        return cla;
    }

    @Override
    public AstNode visitConstruction(MxParser.ConstructionContext ctx) {
        return new Construction(new Position(ctx), ctx.Identifier().getText(), (BlockStatement) visit(ctx.blockStatement()));
    }

    @Override
    public AstNode visitContinueStatement(MxParser.ContinueStatementContext ctx) {
        return new Continue(new Position(ctx));
    }

    @Override
    public AstNode visitExpressionStatement(MxParser.ExpressionStatementContext ctx) {
        return new Expression(new Position(ctx), ctx.expression() == null ? null : (ExpressionNode) visit(ctx.expression()));
    }

    @Override
    public AstNode visitForStatement(MxParser.ForStatementContext ctx) {
        For foor = new For(new Position(ctx));
        if (ctx.forVar().variableDeclaration() != null) {
            foor.var = (VariableDeclaration) visit(ctx.forVar().variableDeclaration());
        } else {
            foor.exp1 = ((Expression) visit(ctx.forVar().expressionStatement())).express;
        }
        if (ctx.statement().blockStatement() != null) {
            foor.sta = ((BlockStatement) visit(ctx.statement().blockStatement())).state;
        } else {
            foor.sta.add((Statements) visit(ctx.statement()));
        }
        if (ctx.expression() != null) {
            foor.exp2 = (ExpressionNode) visit(ctx.expression());
        }
        foor.loopExp = ((Expression) visit(ctx.expressionStatement())).express;
        return foor;
    }

    @Override
    public AstNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        FunctionDefinition func = new FunctionDefinition(new Position(ctx), ctx.Identifier().getText());
        func.returnType = (TypeNode) visit(ctx.returnTypename());
        if (ctx.parameterList() != null) {
            func.para = (ParameterNode) visit(ctx.parameterList());
        }
        func.sta = ((BlockStatement) visit(ctx.blockStatement())).state;
        return func;
    }

    @Override
    public AstNode visitReturnTypename(MxParser.ReturnTypenameContext ctx) {
        if (ctx.Void() != null) {
            return new TypeNode(new Position(ctx), ctx.getText());
        } else {
            return visit(ctx.typename());
        }
    }

    @Override
    public AstNode visitParameterList(MxParser.ParameterListContext ctx) {
        ParameterNode par = new ParameterNode(new Position(ctx));
        for (int i = 0; i < ctx.typename().size(); i++) {
            par.list.add(new Declaration(new Position(ctx.typename(i)), ctx.Identifier(i).getText(), (TypeNode) visit(ctx.typename(i))));
        }
        return par;
    }

    @Override
    public AstNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        VariableDeclaration var = new VariableDeclaration(new Position(ctx));
        TypeNode ty = (TypeNode) visit(ctx.typename());
        for (var vv : ctx.declaration()) {
            var.dec.add(new Declaration(new Position(ctx), vv.Identifier().getText(), ty, vv.expression() == null ? null : (ExpressionNode) visit(vv.expression())));
        }
        return var;
    }

    @Override
    public AstNode visitTypename(MxParser.TypenameContext ctx) {
        return new TypeNode(new Position(ctx), ctx.name().getText(), ctx.LSbrac().size());
    }

    @Override
    public AstNode visitStatement(MxParser.StatementContext ctx) {
        if (ctx.blockStatement() != null)
            return visit(ctx.blockStatement());
        else if (ctx.variableDeclaration() != null)
            return visit(ctx.variableDeclaration());
        else if (ctx.expressionStatement() != null)
            return visit(ctx.expressionStatement());
        else if (ctx.ifStatement() != null)
            return visit(ctx.ifStatement());
        else if (ctx.forStatement() != null)
            return visit(ctx.forStatement());
        else if (ctx.whileStatement() != null)
            return visit(ctx.whileStatement());
        else if (ctx.returnStatement() != null)
            return visit(ctx.returnStatement());
        else if (ctx.breakStatement() != null)
            return visit(ctx.breakStatement());
        else if (ctx.continueStatement() != null)
            return visit(ctx.continueStatement());
        else
            return visitChildren(ctx);
    }

    @Override
    public AstNode visitIfStatement(MxParser.IfStatementContext ctx) {
        If iff = new If(new Position(ctx), (ExpressionNode) visit(ctx.expression()));
        if (ctx.statement(0).blockStatement() != null) {
            iff.trueS = ((BlockStatement) visit(ctx.statement(0).blockStatement())).state;
        } else {
            iff.trueS.add((Statements) visit(ctx.statement(0)));
        }
        if (ctx.Else() != null) {
            if (ctx.statement(1).blockStatement() != null) {
                iff.falseS = ((BlockStatement) visit(ctx.statement(1).blockStatement())).state;
            } else {
                iff.falseS.add((Statements) visit(ctx.statement(1)));
            }
        }

        return iff;
    }

    @Override
    public AstNode visitWhileStatement(MxParser.WhileStatementContext ctx) {
        While whi = new While(new Position(ctx), (ExpressionNode) visit(ctx.expression()));
        if (ctx.statement().blockStatement() != null) {
            whi.sta = ((BlockStatement) visit(ctx.statement().blockStatement())).state;
        } else {
            whi.sta.add((Statements) visit(ctx.statement()));
        }
        return whi;
    }

    @Override
    public AstNode visitReturnStatement(MxParser.ReturnStatementContext ctx) {
        return new Return(new Position(ctx), ctx.expression() == null ? null : (ExpressionNode) visit(ctx.expression()));
    }

    @Override
    public AstNode visitNewEx(MxParser.NewExContext ctx) {
        NewEXp newe = new NewEXp(new Position(ctx), ctx.name().getText());
        newe.dim = ctx.newArrayExpression().size();
        boolean emt = false;
        for (var vv : ctx.newArrayExpression()) {
            if (vv.expression() == null) emt = true;
            else if (emt) {
                throw new eError(new Position(ctx), "wrong array");
            } else newe.List.add((ExpressionNode) visit(vv.expression()));
        }
        return newe;
    }

    @Override
    public AstNode visitUnaryEx(MxParser.UnaryExContext ctx) {
        return new Untary(new Position(ctx), ctx.op.getText(), (ExpressionNode) visit(ctx.expression()));
    }

    @Override
    public AstNode visitLeftEx(MxParser.LeftExContext ctx) {
        return new leftEx(new Position(ctx), ctx.op.getText(), (ExpressionNode) visit(ctx.expression()));
    }

    @Override
    public AstNode visitFunEx(MxParser.FunExContext ctx) {
        FunctionEx func = new FunctionEx(new Position(ctx), (ExpressionNode) visit(ctx.expression()));
        if (ctx.functionCall() != null) {
            func.exps = (FunctionCall) visit(ctx.functionCall());
        }
        return func;
    }

    @Override
    public AstNode visitObjEx(MxParser.ObjExContext ctx) {
        var exp = new ObjectEx(new Position(ctx), (ExpressionNode) visit(ctx.expression()), ctx.Identifier().getText());
        exp.ss = ctx.getText();
        return exp;
    }

    @Override
    public AstNode visitParent(MxParser.ParentContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public AstNode visitFunctionCall(MxParser.FunctionCallContext ctx) {
        FunctionCall func = new FunctionCall(new Position(ctx));
        ctx.expression().forEach(eee -> func.express.add((ExpressionNode) visit(eee)));
        return func;
    }

    @Override
    public AstNode visitTernaryEx(MxParser.TernaryExContext ctx) {
        return new ternaryEx(new Position(ctx), (ExpressionNode) visit(ctx.expression(0)), (ExpressionNode) visit(ctx.expression(1)), (ExpressionNode) visit(ctx.expression(2)));
    }

    @Override
    public AstNode visitFileAnalyze(MxParser.FileAnalyzeContext ctx) {
        FileAnalyze fil = new FileAnalyze(new Position(ctx));
        for (var vv : ctx.children) {
            if (vv instanceof MxParser.ClassDeclarationContext) {
                fil.allFile.add(visit(vv));
            } else if (vv instanceof MxParser.FunctionDeclarationContext) {
                fil.allFile.add(visit(vv));
            } else if (vv instanceof MxParser.VariableDeclarationContext) {
                fil.allFile.add(visit(vv));
            }

        }
        return fil;
    }
}



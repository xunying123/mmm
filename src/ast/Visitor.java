package src.ast;

import src.ast.astnode.expression.*;
import src.ast.astnode.statement.*;
import src.ast.astnode.definition.*;
import src.ast.astnode.*;


public interface Visitor {
    void visit(TypeNode node);

    void visit(VariableDeclaration node);

    void visit(ParameterNode node);

    void visit(Break node);

    void visit(Continue node);

    void visit(Expression node);

    void visit(For node);

    void visit(If node);

    void visit(Return node);

    void visit(While node);

    void visit(ArrayEx node);

    void visit(BinaryEx node);

    void visit(AssignEx node);

    void visit(ObjectEx node);

    void visit(FunctionCall node);

    void visit(FunctionEx node);

    void visit(NewEXp node);

    void visit(Untary node);

    void visit(leftEx node);

    void visit(BlockStatement node);

    void visit(BasicEx node);

    void visit(ternaryEx node);

    void visit(Declaration node);

    void visit(FunctionDefinition node);

    void visit(Construction node);

    void visit (ClassDefinition node);

    void visit (VarEx node);

    void visit(FileAnalyze node);
}

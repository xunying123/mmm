package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Type;
import src.ast.astnode.AstNode;
import src.ast.astnode.definition.FunctionDefinition;
import src.ast.astnode.statement.Expression;

public abstract class ExpressionNode extends AstNode{
    public String ss;
    public Type type;
    public FunctionDefinition func=null;

    public ExpressionNode(Position pos) {
        super(pos);
    }

    public abstract boolean isLeft();

}

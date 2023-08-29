package src.ast.astnode.expression;

import src.IR.basic.IRBasic;
import src.IR.basic.IRRegister;
import src.IR.irtype.IRType;
import src.ast.Position;
import src.ast.Type;
import src.ast.astnode.AstNode;
import src.ast.astnode.definition.FunctionDefinition;

public abstract class ExpressionNode extends AstNode{
    public String ss;
    public Type type;
    public FunctionDefinition func=null;
    public IRBasic value=null;
    public IRRegister store = null;

    public ExpressionNode(Position pos) {
        super(pos);
    }

    public abstract boolean isLeft();

    public IRType getIRType() {
        return value.type;
    }
}

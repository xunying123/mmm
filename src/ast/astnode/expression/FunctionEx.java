package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class FunctionEx extends ExpressionNode{
    public ExpressionNode functi;
    public FunctionCall exps;

    public FunctionEx(Position pos, ExpressionNode exp) {
        super(pos);
        this.functi=exp;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }

    @Override
    public boolean isLeft() {
        return false;
    }
}

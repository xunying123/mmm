package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class ternaryEx extends ExpressionNode{
    public ExpressionNode exp1,exp2,exp3;

    public ternaryEx(Position pos, ExpressionNode exp1, ExpressionNode exp2,ExpressionNode exp3) {
        super(pos);
        this.exp1=exp1;
        this.exp2=exp2;
        this.exp3=exp3;
    }

    @Override
    public boolean isLeft() {
        return exp3.isLeft();
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

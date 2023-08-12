package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class ArrayEx extends ExpressionNode{
    public ExpressionNode array;
    public ExpressionNode index;

    public ArrayEx(Position pos_, ExpressionNode exp1, ExpressionNode exp2) {
        super(pos_);
        this.array=exp1;
        this.index=exp2;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }

    @Override
    public boolean isLeft() {
        return true;
    }
}

package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class Untary extends ExpressionNode{
    public String op;
    public ExpressionNode exp;

    public Untary(Position pos, String op, ExpressionNode exp) {
        super(pos);
        this.exp=exp;
        this.op=op;
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

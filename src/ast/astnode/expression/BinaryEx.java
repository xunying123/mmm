package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class BinaryEx extends ExpressionNode{
    public String op;
    public ExpressionNode leftNode;
    public  ExpressionNode rightNode;

    public BinaryEx(Position pos_, ExpressionNode ll, ExpressionNode rr, String op) {
        super(pos_);
        this.leftNode=ll;
        this.rightNode=rr;
        this.op=op;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

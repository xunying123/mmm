package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class BasicEx extends ExpressionNode{
    public BasicEx(Position pos_,String name) {
        super(pos_);
        this.ss=name;
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

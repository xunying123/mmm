package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class leftEx extends Untary {
    public leftEx(Position pos, String op, ExpressionNode exp) {
        super(pos,op,exp);
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

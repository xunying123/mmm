package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class AssignEx extends BinaryEx {
    public AssignEx(Position pos_, ExpressionNode ll, ExpressionNode rr) {
        super(pos_,ll,rr,"=");
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

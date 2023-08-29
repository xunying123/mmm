package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.expression.ExpressionNode;

public class While extends baseloop{
    public While(Position pos_, ExpressionNode exp) {
        super(pos_);
        this.loopExp =exp;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

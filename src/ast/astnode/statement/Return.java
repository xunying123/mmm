package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.expression.ExpressionNode;

public class Return extends Statements {
    public ExpressionNode exp;

    public Return(Position pos,ExpressionNode exp) {
        super(pos);
        this.exp=exp;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

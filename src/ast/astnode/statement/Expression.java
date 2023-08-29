package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.expression.ExpressionNode;

public class Expression  extends Statements {
    public ExpressionNode express;
    public  Expression(Position pos,ExpressionNode node) {
        super(pos);
        this.express =node;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

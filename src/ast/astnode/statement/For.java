package src.ast.astnode.statement;

import src.IR.basic.IRBlock;
import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.expression.ExpressionNode;
import src.ast.astnode.definition.VariableDeclaration;

public class For extends baseloop {
    public VariableDeclaration var;
    public ExpressionNode exp1,exp2;
    public IRBlock block;

    public For(Position pos_) {
        super(pos_);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

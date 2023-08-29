package src.ast.astnode.statement;

import src.ast.*;

public class Break extends Statements {
    public Break(Position pos_) {
        super(pos_);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

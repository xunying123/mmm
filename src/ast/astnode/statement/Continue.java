package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;

public class Continue extends Statements {
    public Continue(Position pos) {
        super(pos);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

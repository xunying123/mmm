package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;

public class VarEx extends BasicEx{
    public VarEx(Position pos,String ss) {
        super(pos,ss);
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

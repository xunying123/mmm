package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;

import java.util.ArrayList;

public class BlockStatement extends Statements {
    public ArrayList<Statements> state = new ArrayList<>();

    public BlockStatement(Position pos_) {
        super(pos_);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

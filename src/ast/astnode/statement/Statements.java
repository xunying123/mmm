package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.astnode.AstNode;

public abstract class Statements extends AstNode{
    public Statements(Position pos) {
        super(pos);
    }
}

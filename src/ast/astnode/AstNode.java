package src.ast.astnode;

import src.ast.Position;
import src.ast.BuiltIn;
import src.ast.Visitor;

abstract public class AstNode implements BuiltIn {
    public Position pos;

    public AstNode(Position pos_) {
        this.pos=pos_;
    }

    public abstract void accept (Visitor vis);

}

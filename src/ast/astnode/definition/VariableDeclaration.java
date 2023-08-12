package src.ast.astnode.definition;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.statement.Statements;

import java.util.ArrayList;

public class VariableDeclaration extends Statements {
    public ArrayList<Declaration> dec = new ArrayList<>();

    public VariableDeclaration (Position pos) {
        super(pos);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

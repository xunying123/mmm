package src.ast.astnode.expression;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.AstNode;

import java.util.ArrayList;

public class FunctionCall extends AstNode {
    public ArrayList<ExpressionNode> express =new ArrayList<>();

    public FunctionCall(Position pos) {
        super(pos);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

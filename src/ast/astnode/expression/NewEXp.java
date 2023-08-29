package src.ast.astnode.expression;

import src.IR.basic.IRBasic;
import src.ast.Position;
import src.ast.Visitor;

import java.util.ArrayList;

public class NewEXp extends ExpressionNode{
    public String typeName;
    public int dim=0;
    public ArrayList<ExpressionNode> List =new ArrayList<>();
    public IRBasic size;

    public NewEXp(Position pos,String name) {
        super(pos);
        this.typeName = name;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }

}

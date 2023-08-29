package src.ast.astnode.statement;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.expression.ExpressionNode;
import java.util.ArrayList;

public class If extends Statements {
    public ExpressionNode exp;
    public ArrayList<Statements> trueS=new ArrayList<>();
    public ArrayList<Statements> falseS=new ArrayList<>();

    public If (Position pos_,ExpressionNode exp) {
        super(pos_);
        this.exp=exp;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

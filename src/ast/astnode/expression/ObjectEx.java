package src.ast.astnode.expression;

import src.IR.basic.IRRegister;
import src.ast.Position;
import src.ast.Visitor;

public class ObjectEx extends ExpressionNode{
    public ExpressionNode obj;
    public String objTo;
    public IRRegister objAddr;

    public ObjectEx(Position pos, ExpressionNode exp, String toto) {
        super(pos);
        this.obj=exp;
        this.objTo=toto;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

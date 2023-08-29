package src.ast.astnode.statement;

import src.IR.basic.IRBlock;
import src.ast.Position;
import src.ast.astnode.expression.ExpressionNode;

import java.util.ArrayList;

public abstract class baseloop extends Statements {
    public ExpressionNode loopExp;
    public ArrayList<Statements> sta =new ArrayList<>();
    public IRBlock cond;
    public IRBlock loop;
    public IRBlock next;

    public baseloop(Position pos) {
        super(pos);
    }
}

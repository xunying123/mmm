package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRVisitor;
import src.ast.BuiltIn;


public class IRRet extends IRTerminal implements BuiltIn {
    public IRBasic value;

    public IRRet (IRBlock bb,IRBasic v) {
        super(bb);
        this.value=v;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public String toString() {
        return "ret "+value.toStringT();
    }
}

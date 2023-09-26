package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.ast.BuiltIn;

import java.util.LinkedHashSet;


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

    @Override
    public IRRegister getD() {
        return null;
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        LinkedHashSet rr = new LinkedHashSet();
        rr.add(value);
        return rr;
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        value = value==o?(n==null?irInt0:n):value;
    }
}

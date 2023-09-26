package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;

import java.util.LinkedHashSet;


public class IRJump extends IRTerminal{
    public IRBlock block;

    public IRJump(IRBlock b1,IRBlock b2) {
        super(b1);
        this.block=b2;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public String toString() {
        return "br label %"+block.name;
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        return ;
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        return new LinkedHashSet<>();
    }

    @Override
    public IRRegister getD() {
        return null;
    }
}

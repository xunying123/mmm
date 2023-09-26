package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRType;

import java.util.LinkedHashSet;

public class IRAlloca extends IROrders{
    public IRType type;
    public IRRegister alloca;
    public int index;

    public IRAlloca(IRBlock bb,IRType tt,IRRegister aa) {
        super(bb);
        this.type=tt;
        this.alloca=aa;
    }

    public IRAlloca(IRBlock bb,IRType tt,IRRegister aa,int pp) {
        super(bb);
        this.type=tt;
        this.alloca=aa;
        index=pp;
    }

    @Override
    public String toString() {
        return alloca+" = alloca "+type;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public IRRegister getD() {
        return alloca;
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        return new LinkedHashSet<>();
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        return ;
    }
}

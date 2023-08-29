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

    public IRAlloca(IRBlock bb,IRType tt,IRRegister aa) {
        super(bb);
        this.type=tt;
        this.alloca=aa;
    }

    @Override
    public String toString() {
        return alloca+" = alloca "+type;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

}

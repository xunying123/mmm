package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRIntConst;
import src.IR.irtype.IRType;

import java.util.LinkedHashSet;

public class IRCalc extends IROrders{
    public IRType type;
    public String op;
    public IRRegister res;
    public IRBasic ll,rr;

    public IRCalc(IRBlock bb,IRType tt,IRRegister r,IRBasic ll,IRBasic rr,String op) {
        super(bb);
        this.type=tt;
        this.res=r;
        this.op=op;
        this.ll=ll;
        this.rr=rr;
    }

    @Override
    public String toString() {
        return res + " = " +op+" "+ll.toStringT()+", "+rr;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

}

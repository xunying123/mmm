package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRType;

public class IRIcmp extends IROrders{
    public IRType type;
    public IRRegister reg;
    public IRBasic ll,rr;
    public String op;

    public IRIcmp(IRBlock bb,IRType tt,IRRegister rr,IRBasic l,IRBasic r,String oo) {
        super(bb);
        this.type=tt;
        this.reg=rr;
        this.ll=l;
        this.rr=r;
        this.op=oo;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public String toString() {
        return reg+" = icmp "+op+" "+type+" "+ll+", "+rr;
    }

}

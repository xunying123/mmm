package src.IR.instructments;

import src.IR.basic.*;
import src.IR.instructments.IROrders;

import java.util.LinkedHashSet;

public class IRStore extends IROrders {
    public IRBasic value;
    public IRRegister dest;

    public IRStore(IRBlock bb,IRBasic vv,IRRegister rr) {
        super(bb);
        this.value=vv;
        this.dest=rr;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }


    @Override
    public String toString() {
        return "store "+value.toStringT()+", "+dest.toStringT();
    }
}

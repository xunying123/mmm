package src.IR.instructments;

import src.IR.basic.*;

import java.util.LinkedHashSet;


public class IRStore extends IROrders {
    public IRBasic value;
    public IRRegister dest;
    public int index;

    public IRStore(IRBlock bb,IRBasic vv,IRRegister rr) {
        super(bb);
        this.value=vv;
        this.dest=rr;
    }

    public IRStore(IRBlock bb,IRBasic vv,IRRegister rr,int dd) {
        super(bb);
        this.value=vv;
        this.dest=rr;
        this.index=dd;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }


    @Override
    public String toString() {
        return "store "+value.toStringT()+", "+dest.toStringT();
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        if(value==o) {
            value=n;
        }
        if(dest==o) {
            dest = (IRRegister) n;
        }
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        LinkedHashSet<IRBasic> rr = new LinkedHashSet<>();
        rr.add(value);
        rr.add(dest);
        return rr;
    }

    @Override
    public IRRegister getD() {
        return null;
    }
}

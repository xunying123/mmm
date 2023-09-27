package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRCondConst;
import src.IR.irtype.IRIntConst;
import src.IR.irtype.IRType;

import java.util.LinkedHashSet;

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

    @Override
    public IRRegister getD() {
        return reg;
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        LinkedHashSet<IRBasic> r = new LinkedHashSet<>();
        r.add(ll);
        r.add(rr);
        return r;
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        ll=ll==o?n:ll;
        rr=rr==o?n:rr;
    }

    public IRCondConst calc() {
        if (ll instanceof IRIntConst && rr instanceof IRIntConst) {
            int lll = ((IRIntConst) ll).value;
            int rrr = ((IRIntConst) rr).value;
            boolean re = false;
            switch (op) {
                case "eq":
                    re = lll == rrr;
                    break;
                case "ne":
                    re = lll != rrr;
                    break;
                case "sgt":
                    re = lll > rrr;
                    break;
                case "sge":
                    re = lll >= rrr;
                    break;
                case "slt":
                    re = lll < rrr;
                    break;
                case "sle":
                    re = lll <= rrr;
                    break;
            }
            return new IRCondConst(re);
        }
        return null;
    }
}

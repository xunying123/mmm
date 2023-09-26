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

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        ll=ll==o?n:ll;
        rr=rr==o?n:rr;
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        LinkedHashSet<IRBasic> r = new LinkedHashSet<>();
        r.add(ll);
        r.add(rr);
        return r;
    }

    @Override
    public IRRegister getD() {
        return res;
    }

    public IRIntConst calcConst() {
        if(ll instanceof IRIntConst && rr instanceof IRIntConst) {
            int lll = ((IRIntConst) ll).value;
            int rrr = ((IRIntConst) rr).value;
            int r = 0;
            switch (op) {
                case "add":
                    r = lll + rrr;
                    break;
                case "sub":
                    r = lll - rrr;
                    break;
                case "mul":
                    r = lll * rrr;
                    break;
                case "sdiv":
                    if (rrr == 0) return null;
                    r = lll / rrr;
                    break;
                case "srem":
                    r = lll % rrr;
                    break;
                case "shl":
                    r = lll << rrr;
                    break;
                case "ashr":
                    r = lll >> rrr;
                    break;
                case "and":
                    r = lll & rrr;
                    break;
                case "or":
                    r = lll | rrr;
                    break;
                case "xor":
                    r = lll ^ rrr;
                    break;
            }
            return new IRIntConst(r);
        }
        return null;
    }
}

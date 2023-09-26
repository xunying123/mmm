package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class IRPhi extends IROrders{
    public IRRegister dest,src;
    public ArrayList<IRBasic> vv = new ArrayList<>();
    public ArrayList<IRBlock> bb = new ArrayList<>();

    public IRPhi(IRBlock b,IRRegister s,IRRegister d) {
        super(b);
        this.src=s;
        this.dest=d;
    }

    public void add(IRBasic v, IRBlock b) {
        this.vv.add(v==null?dest.type.defaultValue():v);
        this.bb.add(b);
    }

    @Override
    public String toString() {
        String ret = dest + " = phi "+dest.type+" ";
        for(int i=0;i<vv.size();i++) {
            ret += "[ "+vv.get(i) +", %"+bb.get(i).name+" ]";
            if(i!=vv.size()-1) ret+=", ";
        }
        return ret;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public void replaceU(IRBasic o, IRBasic n) {
        for(int i=0;i<vv.size();i++) {
            vv.set(i,vv.get(i)==o?n:vv.get(i));
        }
    }

    @Override
    public LinkedHashSet<IRBasic> getU() {
        LinkedHashSet rr = new LinkedHashSet();
        for(IRBasic v:vv) {
            rr.add(v);
        }
        return rr;
    }

    @Override
    public IRRegister getD() {
        return dest;
    }
}

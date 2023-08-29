package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRPtr;
import src.IR.irtype.IRType;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class IRGetelementptr extends IROrders{
    public IRRegister res;
    public IRBasic ptr;
    public IRType type;
    public ArrayList<IRBasic> index = new ArrayList<>();

    public IRGetelementptr(IRBlock bb,IRBasic pp,IRRegister rr,IRBasic... ii) {
        super(bb);
        this.ptr=pp;
        this.type=((IRPtr) ptr.type).PointTo();
        this.res=rr;
        for(IRBasic in:ii) {
            this.index.add(in);
        }
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public String toString() {
        String r = res+" = getelementptr "+type+", "+ptr.toStringT();
        for(IRBasic ii:index) {
            r+=", "+ii.toStringT();
        }
        return r;
    }
}

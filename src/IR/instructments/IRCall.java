package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRType;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class IRCall extends IROrders{
    public IRType type;
    public ArrayList<IRBasic> args = new ArrayList<>();
    public IRRegister call;
    public String name;

    public IRCall(IRBlock bb,IRType tt,String na) {
        super(bb);
        this.type=tt;
        this.name=na;
    }

    public IRCall(IRBlock bb,IRRegister ca,IRType tt,String na,IRBasic... aa) {
        super(bb);
        this.type=tt;
        this.call=ca;
        this.name=na;
        for(IRBasic a:aa) {
            this.args.add(a);
        }
    }

    @Override
    public String toString() {
        String rrr = (call!=null?call+" = call ":"call ")+type+" @"+name+"(";
        for(int i=0;i<args.size();i++) {
            rrr+=args.get(i).toStringT();
            if(i!=args.size()-1) rrr+=", ";
        }
        rrr+=")";
        return rrr;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }
}

package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRType;

public class IRLoad extends IROrders{
    public IRBasic src;
    public IRRegister reg;
    public IRType type;

    public IRLoad(IRBlock bb,IRRegister rr,IRBasic ss) {
        super(bb);
        this.reg=rr;
        this.src=ss;
        this.type=rr.type;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

    @Override
    public String toString() {
        return reg+" = load "+type+", "+src.toStringT();
    }
}

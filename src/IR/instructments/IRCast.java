package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;
import src.IR.irtype.IRType;

public abstract class IRCast extends IROrders{
    public IRBasic value;
    public IRType type;
    public IRRegister dest;

    public IRCast(IRBlock bb,IRBasic value,IRType tt,IRRegister de) {
        super(bb);
        this.value=value;
        this.type=tt;
        this.dest=de;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

}

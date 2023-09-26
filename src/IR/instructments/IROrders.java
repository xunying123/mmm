package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;

import java.util.LinkedHashSet;

public abstract class IROrders {
    public IRBlock parent;
    public boolean delete=false;

    public IROrders(IRBlock bb) {
        this.parent=bb;
    }

    public abstract void accept(IRVisitor vis);

    public abstract LinkedHashSet<IRBasic> getU();

    public abstract IRRegister getD();

    public abstract void replaceU(IRBasic o, IRBasic n);
}

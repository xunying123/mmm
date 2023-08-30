package src.IR.instructments;

import src.IR.basic.IRBlock;
import src.IR.basic.IRVisitor;

public abstract class IROrders {
    public IRBlock parent;

    public IROrders(IRBlock bb) {
        this.parent=bb;
    }

    public abstract void accept(IRVisitor vis);

}

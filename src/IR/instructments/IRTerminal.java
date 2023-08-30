package src.IR.instructments;

import src.IR.basic.IRBlock;

public abstract class IRTerminal extends IROrders{
    public IRTerminal(IRBlock bb) {
        super(bb);
    }
}

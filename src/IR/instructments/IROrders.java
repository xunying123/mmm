package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.basic.IRVisitor;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public abstract class IROrders {
    public IRBlock parent;

    public IROrders(IRBlock bb) {
        this.parent=bb;
    }

    public abstract void accept(IRVisitor vis);

}

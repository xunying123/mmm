package src.IR.basic;

import src.IR.instructments.*;

import java.util.LinkedList;

public class IRBlock {
    public String name;
    public IRFunction parent;
    public LinkedList<IROrders> insts = new LinkedList<>();
    public IRTerminal ter = null;
    public boolean isFinished = false;
    public static int cnt = 0;

    public IRBlock(IRFunction fun, String name) {
        this.parent = fun;
        this.name = name +(cnt++);

    }

    public IRBlock(IRFunction fun, String na, IRBlock tot) {
        this.parent = fun;
        this.name = na + (cnt++);
        this.ter = new IRJump(this, tot);
    }

    public void add(IROrders ooo) {
        if (isFinished) return;
        if (ooo instanceof IRAlloca) {
            parent.alloca.add((IRAlloca) ooo);
        } else if (ooo instanceof IRTerminal) {
            ter = (IRTerminal) ooo;
        } else {
            insts.add(ooo);
        }
    }

    public String toString() {
        String rr = name + ":\n";
        for (IROrders o : insts) rr += "  " + o + "\n";
        if (ter != null) rr += "  " + ter + "\n";
        return rr;
    }

    public void accept(IRVisitor vis) {
        vis.visit(this);
    }
}

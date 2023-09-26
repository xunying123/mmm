package src.IR.basic;

import src.IR.instructments.*;

import java.util.LinkedList;

public class IRBlock {
    public String name;
    public IRFunction parent;
    public LinkedList<IROrders> insts = new LinkedList<>();
    public IRTerminal ter = null;
    public boolean isFinished = false;
    public int depth;
    public static int cnt = 0;
    public IRBlock oo = null;
    public LinkedList<IRBlock> pred = new LinkedList<>();
    public LinkedList<IRBlock> succ = new LinkedList<>();
    public LinkedList<IRBlock> domC = new LinkedList<>();
    public LinkedList<IRBlock> domF = new LinkedList<>();
    public LinkedList<IRPhi> phi = new LinkedList<>();

    public IRBlock(IRFunction fun, String name,int dd) {
        this.parent = fun;
        this.name = name +(cnt++);
        this.depth=dd;
    }

    public IRBlock(IRFunction fun, String na, IRBlock tot,int dd) {
        this.parent = fun;
        this.name = na + (cnt++);
        this.ter = new IRJump(this, tot);
        this.depth=dd;
    }

    public void add(IROrders ooo) {
        if(ooo instanceof IRPhi pp) {
            for(IRPhi ee:phi) {
                if(pp.src == ee.src) return;
            }
            phi.add((IRPhi) ooo);
        } else {
            if (isFinished) return;
            if (ooo instanceof IRAlloca) {
                parent.alloca.add((IRAlloca) ooo);
            } else if (ooo instanceof IRTerminal) {
                ter = (IRTerminal) ooo;
            } else {
                insts.add(ooo);
            }
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

package src.IR.basic;

import src.IR.instructments.IRAlloca;
import src.IR.instructments.IRBranch;
import src.IR.instructments.IROrders;
import src.IR.instructments.IRRet;
import src.IR.irtype.IRType;

import java.util.*;

public class IRFunction {
    public String name;
    public IRType returnType;
    public LinkedList<IRBlock> blocks = new LinkedList<>();
    public ArrayList<IRRegister> para = new ArrayList<>();
    public ArrayList<IRAlloca> alloca = new ArrayList<>();
    public IRBlock exit;
    public IRBlock entry;
    public IRRegister ret;

    public IRFunction(String name_, IRType tt) {
        this.name = name_;
        this.returnType = tt;
    }

    public IRBlock add(IRBlock bb) {
        blocks.add(bb);
        return bb;
    }

    public void finish() {
        entry = blocks.getFirst();
        for (int i = alloca.size() - 1; i >= 0; i--) {
            entry.insts.addFirst(alloca.get(i));
        }
        blocks.add(exit);
    }

    public String toString() {
        String rr = "define " + returnType.toString() + " @" + name + "(";
        IRRegister.cnt = 0;
        for (int i = 0; i < para.size(); i++) {
            rr += para.get(i).toStringT();
            if (i != para.size() - 1) rr += ", ";
        }
        rr += ") {\n";
        for (IRBlock bb : blocks) rr += bb.toString();
        rr += "}\n";
        return rr;
    }

    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

}

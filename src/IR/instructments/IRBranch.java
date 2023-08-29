package src.IR.instructments;

import src.IR.basic.*;

import java.util.LinkedHashSet;

public class IRBranch extends IRTerminal{
    public IRBasic cond;
    public IRBlock trueB;
    public IRBlock falseB;

    public IRBranch(IRBlock bb,IRBasic cond,IRBlock tt,IRBlock ff) {
        super(bb);
        this.cond=cond;
        this.trueB=tt;
        this.falseB=ff;
    }

    @Override
    public String toString() {
        return "br " + cond.toStringT()+", label %" +trueB.name+", label %"+falseB.name;
    }

    @Override
    public void accept(IRVisitor vis) {
        vis.visit(this);
    }

}

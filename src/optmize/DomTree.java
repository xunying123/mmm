package src.optmize;

import src.IR.basic.IRBlock;
import src.IR.basic.IRFileAnalyze;
import src.IR.basic.IRFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DomTree {
    IRFileAnalyze irF;
    HashSet<IRBlock> vv = new HashSet<>();
    HashMap<IRBlock,Integer> oo = new HashMap<>();
    LinkedList<IRBlock> bb = new LinkedList<>();

    public DomTree(IRFileAnalyze f) {
        this.irF=f;
    }

    public void work() {
        irF.fuc.forEach(this::workF);
    }

    public void workF(IRFunction func) {
        vv.clear();
        oo.clear();
        bb.clear();
        calc(func.entry);
        func.entry.oo=func.entry;
        bb.removeFirst();
        boolean isChan = true;
        while (isChan) {
            isChan=false;
            for(IRBlock b :bb) {
                IRBlock temp = null;
                for(IRBlock pr :b.pred) {
                    if(temp==null) temp=pr;
                    else if(pr.oo!=null) temp = cmp(pr,temp);
                }
                if(temp!=b.oo) {
                    b.oo=temp;
                    isChan=true;
                }
            }
        }
        bb.forEach(b -> b.oo.domC.add(b));
        bb.addFirst(func.entry);
        for(IRBlock b : bb) {
            if(b.pred.size()<2) continue;
            for(IRBlock pr:b.pred) {
                IRBlock rr = pr;
                while(rr!=b.oo) {
                    rr.domF.add(b);
                    rr = rr.oo;
                }
            }
        }
    }

    public void calc(IRBlock b) {
        vv.add(b);
        for(var su : b.succ) {
            if(!vv.contains(su)) calc(su);
        }
        oo.put(b,bb.size());
        bb.addFirst(b);
    }

   public IRBlock cmp(IRBlock x,IRBlock y) {
        while(x!=y) {
            while(oo.get(x)<oo.get(y)) x=x.oo;
            while(oo.get(y)<oo.get(x)) y=y.oo;
        }
        return x;
    }
}


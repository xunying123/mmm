package src.optmize;

import src.IR.basic.IRFileAnalyze;
import src.IR.basic.IRFunction;
import src.IR.basic.IRGlobalVar;
import src.IR.basic.IRRegister;
import src.IR.instructments.IRAlloca;
import src.IR.instructments.IRStore;
import src.IR.irtype.IRPtr;
import src.asm.basic.Global;

import java.util.ArrayList;

public class GlobalTo {
    IRFileAnalyze irF;

    public GlobalTo(IRFileAnalyze ff) {
        this.irF = ff;
    }

    public void work() {
        var newL = new ArrayList<IRGlobalVar>();
        for(var gg : irF.var) {
            if(gg.isCall) {
                newL.add(gg);
                continue;
            }
            IRFunction in = null;
            boolean isIN  = true;
            for(var ff : irF.fuc) {
                for(var bb : ff.blocks) {
                    for(var ii : bb.insts) {
                        if(ii.getU().contains(gg)) {
                            if(in ==null) in = ff;
                            else if(in !=ff) {
                                isIN = false;
                                break;
                            }
                        }
                    }
                }
            }
            if(isIN && in!=null && (in == irF.mainFunc || in == irF.initFunc)) {
                IRRegister rr = new IRRegister("global",gg.type);
                in.alloca.add(new IRAlloca(in.entry,((IRPtr) gg.type).PointTo(),rr));
                in.entry.insts.addFirst(new IRStore(in.entry,gg.initValue,rr));
                for(var b :in.blocks)
                    for(var i : b.insts) i.replaceU(gg,rr);
            } else if(in !=null) {
                newL.add(gg);
            }
        }
        irF.var=newL;
        for(var ff:irF.fuc) ff.finish();
    }
}

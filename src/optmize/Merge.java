package src.optmize;

import src.asm.basic.AsmBlock;
import src.asm.basic.AsmFile;
import src.asm.basic.AsmFunction;
import src.asm.instructions.AsmJump;

import java.util.ArrayList;

public class Merge {
    AsmFile asmF;

    public Merge(AsmFile a) {
        this.asmF=a;
    }

    public void work() {
        asmF.fun.forEach(this::workF);
    }

    public void workF(AsmFunction ff) {
        ArrayList<AsmBlock> nB  =new ArrayList<>();
        nB.add(ff.block.get(0));
        for(int i=1;i<ff.block.size();i++) {
            AsmBlock bb = ff.block.get(i);
            AsmBlock lb = nB.get(nB.size()-1);
            if(bb.pred.size()==0) {
                for(AsmBlock ss : bb.succ) ss.pred.remove(bb);
                continue;
            }
            if(bb.pred.size()==1 && bb.pred.get(0)==lb && lb.insts.get(lb.insts.size()-1) instanceof AsmJump jj && jj.to == bb) {
                lb.insts.removeLast();
                lb.insts.addAll(bb.insts);
                lb.succ.remove(bb);
                lb.succ.addAll(bb.succ);
                for(AsmBlock su : bb.succ) {
                    su.pred.remove(bb);
                    su.pred.add(lb);
                }
            } else {
                nB.add(bb);
            }
        }
        ff.block=nB;
    }
}

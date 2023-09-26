package src.optmize;

import src.IR.basic.IRBlock;
import src.IR.basic.IRFileAnalyze;
import src.IR.basic.IRFunction;
import src.IR.instructments.IRBranch;
import src.IR.instructments.IRJump;

import java.util.LinkedList;

public class CFG {
    IRFileAnalyze irF;

    public CFG(IRFileAnalyze ii) {
        this.irF=ii;
    }

    public void work() {
        irF.fuc.forEach(this::workF);
    }

    public void workF(IRFunction ff) {
        ff.blocks.forEach(bb -> {
            if(bb.ter instanceof IRJump) {
                IRJump jj = (IRJump) bb.ter;
                bb.succ.add(jj.block);
                jj.block.pred.add(bb);
            } else if(bb.ter instanceof IRBranch){
                IRBranch br = (IRBranch) bb.ter;
                bb.succ.add(br.trueB);
                bb.succ.add(br.falseB);
                br.trueB.pred.add(bb);
                br.falseB.pred.add(bb);
            }
        });

        LinkedList<IRBlock> nB = new LinkedList<>();
        for(var bb:ff.blocks) {
            if(!bb.pred.isEmpty() || bb == ff.entry) nB.add(bb);
            else {
                for(var ss : bb.succ) ss.pred.remove(bb);
            }
        }
        ff.blocks=nB;
    }
}

package src.optmize;

import src.asm.basic.AsmBlock;
import src.asm.basic.AsmFile;
import src.asm.basic.AsmFunction;
import src.asm.basic.AsmReg;
import src.asm.instructions.AsmInstruction;

import java.util.HashSet;
import java.util.LinkedList;

public class LiveAnalyze {
    AsmFunction asmF;
    LinkedList<AsmBlock> list = new LinkedList<>();
    HashSet<AsmBlock> in =new HashSet<>();

    public LiveAnalyze(AsmFunction ff) {
        this.asmF = ff;
        for(AsmBlock bb :ff.block) {
            bb.in.clear();
            bb.out.clear();
        }
        for(AsmBlock bb : ff.block) {
            bb.use.clear();
            bb.def.clear();
            for(AsmInstruction ii : bb.insts) {
                for(var rr : ii.getU()) {
                    if(!bb.def.contains(rr)) bb.use.add(rr);
                }
                bb.def.addAll(ii.getD());
            }
        }
    }

    public void work() {
        list.clear();
        in.clear();
        list.add(asmF.exit);
        in.add(asmF.exit);
        while(!list.isEmpty()) {
            AsmBlock bb = list.removeFirst();
            in.remove(bb);
            HashSet<AsmReg> nwO = new HashSet<>();
            for(var su : bb.succ) nwO.addAll(su.in);
            HashSet<AsmReg> nwI = new HashSet<>(bb.use);
            nwI.addAll(nwO);
            nwI.removeAll(bb.def);
            if(!nwI.equals(bb.in) || !nwO.equals(bb.out)) {
                bb.in = nwI;
                bb.out=nwO;
                for(var pr : bb.pred) {
                    if(!in.contains(pr)) {
                        list.add(pr);
                        in.add(pr);
                    }
                }
            }
        }
    }
}

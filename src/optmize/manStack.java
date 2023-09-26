package src.optmize;

import src.asm.basic.AsmFile;
import src.asm.basic.AsmRealReg;
import src.asm.basic.AsmVirtualImm;
import src.asm.basic.Immediate;
import src.asm.instructions.AsmBinary;
import src.asm.instructions.AsmLi;
import src.asm.instructions.AsmRet;
import src.asm.instructions.AsmUnary;

public class manStack {
    AsmFile asmF;

    public manStack(AsmFile aa) {
        this.asmF = aa;
    }

    public void work() {
        for(var cur : asmF.fun) {
            int total = cur.paraU+cur.alloca+cur.spillU;
            if(total<1<<11) cur.entry.insts.addFirst(new AsmUnary("addi", AsmRealReg.get("sp"),AsmRealReg.get("sp"),new Immediate(-total)));
            else {
                cur.entry.insts.addFirst(new AsmBinary("add", AsmRealReg.get("sp"),AsmRealReg.get("sp"),AsmRealReg.get("t0")));
                cur.entry.insts.addFirst(new AsmLi(AsmRealReg.get("t0"),new AsmVirtualImm(-total)));
            }

            if(total<1<<11) cur.exit.insts.addFirst(new AsmUnary("addi", AsmRealReg.get("sp"),AsmRealReg.get("sp"),new Immediate(total)));
            else {
                cur.exit.insts.addFirst(new AsmLi(AsmRealReg.get("t0"),new AsmVirtualImm(total)));
                cur.exit.insts.addFirst(new AsmBinary("add", AsmRealReg.get("sp"),AsmRealReg.get("sp"),AsmRealReg.get("t0")));
            }
            cur.exit.insts.add(new AsmRet());
        }
    }
}

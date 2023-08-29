package src.codegen;

import src.asm.basic.*;
import src.asm.instructions.*;

import java.util.LinkedList;

public class RegAllocator {
    AsmFile filea;
    int total;
    int regStart;
    AsmRealReg t0 = AsmRealReg.regMap.get("t0");
    AsmRealReg t1 = AsmRealReg.regMap.get("t1");
    AsmRealReg t2 = AsmRealReg.regMap.get("t2");
    AsmRealReg sp = AsmRealReg.regMap.get("sp");
    LinkedList<AsmInstruction> list;

    public RegAllocator(AsmFile aa) {
        this.filea=aa;
    }

    public void work() {
        for(AsmFunction ff : filea.fun) {
            total = ff.all;
            regStart = ff.paraU+ff.alloca;
            for(AsmBlock bb : ff.block) visitB(bb);
        }
    }

    public void visitB(AsmBlock bb) {
        list = new LinkedList<>();
        for(AsmInstruction ii : bb.insts) {
            if(ii.rs1!=null && !(ii.rs1 instanceof AsmRealReg)) {
                alloSrc(t1,ii.rs1);
                ii.rs1=t1;
            }
            if(ii.rs2!=null && !(ii.rs2 instanceof AsmRealReg)) {
                alloSrc(t0,ii.rs2);
                ii.rs2=t0;
            }
            list.add(ii);
            if(ii.rd!=null && !(ii.rd instanceof AsmRealReg)) {
                alloDes(t0,ii.rd);
                ii.rd=t0;
            }
        }
        bb.insts = list;
    }

    void alloSrc(AsmRealReg rr, AsmReg ss) {
        if(ss instanceof AsmVirtualReg) {
            int off = ((AsmVirtualReg) ss).id !=-1?regStart+((AsmVirtualReg) ss).id*4:total+((AsmVirtualReg) ss).index*4;
            if(off<1<<11) {
                list.add(new AsmLoad(((AsmVirtualReg) ss).size,rr,sp,new Immediate(off)));
            } else {
                list.add(new AsmLi(t2,new AsmVirtualImm(off)));
                list.add(new AsmBinary("add",t2,t2,sp));
                list.add(new AsmLoad(((AsmVirtualReg) ss).size,rr,t2));
            }

        }else if(ss instanceof AsmVirtualImm) {
            list.add(new AsmLi(rr,(AsmVirtualImm) ss));
        } else if(ss instanceof Global) {
            list.add(new AsmLui(rr,new HiLoFun(HiLoFun.Type.hi,((Global) ss).name)));
            list.add(new AsmUnary("addi",rr,rr,new HiLoFun(HiLoFun.Type.lo,((Global) ss).name)));
        }
    }

    void alloDes(AsmRealReg rr,AsmReg dd) {
        if(dd instanceof AsmVirtualReg) {
            int off = ((AsmVirtualReg) dd).id !=-1?regStart+((AsmVirtualReg) dd).id*4:total+((AsmVirtualReg) dd).index*4;
            if(off<1<<11) {
                list.add(new AsmStore(((AsmVirtualReg) dd).size,sp,rr,new Immediate(off)));
            } else {
                list.add(new AsmLi(t2,new AsmVirtualImm(off)));
                list.add(new AsmBinary("add",t2,t2,sp));
                list.add(new AsmStore(((AsmVirtualReg) dd).size,t2,rr));
            }
        }
    }
}

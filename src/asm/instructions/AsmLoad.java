package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;

public class AsmLoad extends AsmInstruction{
    int size;

    public AsmLoad(int si, AsmReg rr, AsmReg rr1) {
        this.size=si;
        this.rd=rr;
        this.rs1=rr1;
        this.imm=new Immediate(0);
    }

    public AsmLoad(int si, AsmReg rr, AsmReg rr1, Immediate ii) {
        this(si,rr,rr1);
        this.imm=ii;
    }

    @Override
    public String toString() {
        return "l"+(size==1?"b":"w")+" "+rd +", "+imm+"("+rs1+")";
    }
}



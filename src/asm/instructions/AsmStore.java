package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;

public class AsmStore extends AsmInstruction{
    int size;

    public AsmStore(int si, AsmReg rr1, AsmReg rr2) {
        this.size=si;
        this.rs1=rr1;
        this.rs2=rr2;
        this.imm=new Immediate(0);
    }

    public AsmStore(int si, AsmReg rr1, AsmReg rr2, Immediate ii) {
        this(si,rr1,rr2);
        this.imm=ii;
    }

    @Override
    public String toString() {
        return "s"+(size==1?"b":"w")+" "+rs2 +", "+imm+"("+rs1+")";
    }
}

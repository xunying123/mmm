package src.asm.instructions;

import src.asm.basic.AsmReg;

public class AsmMv extends AsmInstruction{
    public AsmMv(AsmReg rr, AsmReg rr1) {
        this.rd=rr;
        this.rs1=rr1;
    }

    @Override
    public String toString() {
        return "mv "+rd+", "+rs1;
    }
}

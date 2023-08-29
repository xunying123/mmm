package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;

public class AsmLui extends AsmInstruction{
    public AsmLui(AsmReg rr, Immediate ii) {
        this.imm=ii;
        this.rd=rr;
    }

    @Override
    public String toString() {
        return "lui "+rd+", "+imm;
    }
}

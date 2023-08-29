package src.asm.instructions;

import src.asm.basic.AsmRealReg;
import src.asm.basic.AsmReg;
import src.asm.basic.AsmVirtualImm;

public class AsmLi extends AsmInstruction{
    public AsmVirtualImm imm;

    public AsmLi(AsmReg rr, AsmVirtualImm ii) {
        this.imm=ii;
        this.rd=rr;
    }

    @Override
    public String toString() {
        return "li "+rd +", "+imm;
    }
}

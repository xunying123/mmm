package src.asm.instructions;

import src.asm.basic.AsmBlock;
import src.asm.basic.AsmReg;

public class AsmBeq extends AsmInstruction{
    AsmBlock to;

    public AsmBeq(AsmReg rr, AsmBlock bb) {
        this.to=bb;
        this.rs1 = rr;
    }

    @Override
    public String toString() {
        return "beqz "+rs1 + ", " + to.name;
    }
}

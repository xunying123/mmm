package src.asm.instructions;

import src.asm.basic.AsmBlock;
import src.asm.basic.AsmReg;

public class AsmBr extends AsmInstruction {
    String op;
    AsmBlock bb;

    public AsmBr(String oo, AsmReg r1, AsmReg r2, AsmBlock to) {
        this.bb = to;
        this.op = oo;
        this.rs1 = r1;
        this.rs2 = r2;
    }

    @Override
    public String toString() {
        return op + " " + rs1 + ", " + rs2 + ", " + bb.name;
    }
}

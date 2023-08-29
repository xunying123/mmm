package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;

public abstract class AsmInstruction {
    public AsmReg rd;
    public AsmReg rs1;
    public AsmReg rs2;
    public Immediate imm;

    public abstract String toString();
}

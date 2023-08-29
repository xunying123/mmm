package src.asm.instructions;

import src.asm.basic.AsmReg;

public class AsmBinary extends AsmInstruction {
    String op;

    public AsmBinary(String oo, AsmReg rr, AsmReg rr1, AsmReg rr2) {
        switch (oo) {
            case "sdiv" -> this.op = "div";
            case "srem" -> this.op = "rem";
            case "shl" -> this.op = "sll";
            case "ashr" -> this.op = "sra";
            default -> this.op = oo;
        }
        this.rd = rr;
        this.rs1 = rr1;
        this.rs2 = rr2;
    }

    @Override
    public String toString() {
        return op + " " + rd + ", " + rs1 + ", " + rs2;
    }
}

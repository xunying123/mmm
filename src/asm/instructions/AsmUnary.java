package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;


public class AsmUnary extends AsmInstruction {
    String op;

    public AsmUnary(String oo, AsmReg rr, AsmReg rr1) {
        this.op = oo;
        this.rd=rr;
        this.rs1=rr1;

    }

    public AsmUnary(String oo, AsmReg rr, AsmReg rr1, Immediate ii) {
        this(oo,rr,rr1);
        this.imm=ii;
    }

    @Override
    public String toString() {
        if(imm==null) return op+" "+rd +", "+rs1;
        else return op + " "+rd + ", "+rs1+ "," +imm;
    }
}

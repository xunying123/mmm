package src.asm.instructions;

import src.asm.basic.AsmReg;
import src.asm.basic.Immediate;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AsmInstruction {
    public AsmReg rd;
    public AsmReg rs1;
    public AsmReg rs2;
    public Immediate imm;
    public HashSet<AsmReg> in = new HashSet<>();
    public HashSet<AsmReg> out = new HashSet<>();

    public abstract String toString();

    public HashSet<AsmReg> getU() {
        HashSet<AsmReg> rr = new HashSet<>();
        if(rs1!=null) rr.add(rs1);
        if(rs2!=null) rr.add(rs2);
        return rr;
    }

    public HashSet<AsmReg> getD() {
        HashSet<AsmReg> rr = new HashSet<>();
        if(rd!=null) rr.add(rd);
        return rr;
    }
}

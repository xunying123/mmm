package src.asm.instructions;

import src.asm.basic.AsmRealReg;
import src.asm.basic.AsmReg;

import java.util.HashSet;

public class AsmCall extends AsmInstruction{
    String name;
    HashSet<AsmReg> uu = new HashSet<>();
    static HashSet<AsmReg> dd = new HashSet<>(AsmRealReg.caller);

    public AsmCall(String nn) {
        this.name=nn;
    }

    @Override
    public String toString() {
        return "call "+name;
    }

    @Override
    public HashSet<AsmReg> getD() {
        return dd;
    }

    @Override
    public HashSet<AsmReg> getU() {
        return uu;
    }

    public void addU(AsmRealReg rr) {
        uu.add(rr);
    }
}

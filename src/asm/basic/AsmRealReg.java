package src.asm.basic;

import java.util.HashMap;

public class AsmRealReg extends AsmReg {
    public String name;

    public AsmRealReg(String na) {
        this.name = na;
    }

    public static HashMap<String, AsmRealReg> regMap = new HashMap<>() {
        {
            put("zero", new AsmRealReg("zero"));
            put("ra", new AsmRealReg("ra"));
            put("sp", new AsmRealReg("sp"));
            put("t0", new AsmRealReg("t0"));
            put("t1", new AsmRealReg("t1"));
            put("t2", new AsmRealReg("t2"));
            put("a0", new AsmRealReg("a0"));
            put("a1", new AsmRealReg("a1"));
            put("a2", new AsmRealReg("a2"));
            put("a3", new AsmRealReg("a3"));
            put("a4", new AsmRealReg("a4"));
            put("a5", new AsmRealReg("a5"));
            put("a6", new AsmRealReg("a6"));
            put("a7", new AsmRealReg("a7"));
        }
    };

    @Override
    public String toString() {
        return name;
    }
}

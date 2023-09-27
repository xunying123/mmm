package src.asm.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AsmRealReg extends AsmReg {
    public String name;
    public int id;
    public static HashMap<String, AsmRealReg> regMap = new HashMap<>() {
        {
            put("zero", new AsmRealReg("zero", 0));
            put("ra", new AsmRealReg("ra", 1));
            put("sp", new AsmRealReg("sp", 2));
            put("gp", new AsmRealReg("gp", 3));
            put("tp", new AsmRealReg("tp", 4));
            put("t0", new AsmRealReg("t0", 5));
            put("t1", new AsmRealReg("t1", 6));
            put("t2", new AsmRealReg("t2", 7));
            put("s0", new AsmRealReg("s0", 8));
            put("s1", new AsmRealReg("s1", 9));
            put("a0", new AsmRealReg("a0", 10));
            put("a1", new AsmRealReg("a1", 11));
            put("a2", new AsmRealReg("a2", 12));
            put("a3", new AsmRealReg("a3", 13));
            put("a4", new AsmRealReg("a4", 14));
            put("a5", new AsmRealReg("a5", 15));
            put("a6", new AsmRealReg("a6", 16));
            put("a7", new AsmRealReg("a7", 17));
            put("s2", new AsmRealReg("s2", 18));
            put("s3", new AsmRealReg("s3", 19));
            put("s4", new AsmRealReg("s4", 20));
            put("s5", new AsmRealReg("s5", 21));
            put("s6", new AsmRealReg("s6", 22));
            put("s7", new AsmRealReg("s7", 23));
            put("s8", new AsmRealReg("s8", 24));
            put("s9", new AsmRealReg("s9", 25));
            put("s10", new AsmRealReg("s10", 26));
            put("s11", new AsmRealReg("s11", 27));
            put("t3", new AsmRealReg("t3", 28));
            put("t4", new AsmRealReg("t4", 29));
            put("t5", new AsmRealReg("t5", 30));
            put("t6", new AsmRealReg("t6", 31));
        }
    };

    public static HashSet<AsmReg> caller = new HashSet<>() {
        {
            add(regMap.get("ra"));
            for (int i = 0; i < 7; i++) add(regMap.get("t" + i));
            for (int i = 0; i < 8; i++) add(regMap.get("a" + i));
        }
    };

    public static HashSet<AsmReg> callee = new HashSet<>() {
        {
            for (int i = 0; i < 12; i++) add(regMap.get("s" + i));
        }
    };

    public static ArrayList<AsmReg> idR = new ArrayList<>() {
        {
            add(regMap.get("zero"));
            add(regMap.get("ra"));
            add(regMap.get("sp"));
            add(regMap.get("gp"));
            add(regMap.get("tp"));
            for (int i = 0; i < 3; i++) add(regMap.get("t" + i));
            for (int i = 0; i < 2; i++) add(regMap.get("s" + i));
            for (int i = 0; i < 8; i++) add(regMap.get("a" + i));
            for (int i = 2; i < 12; i++) add(regMap.get("s" + i));
            for (int i = 3; i < 7; i++) add(regMap.get("t" + i));
        }
    };

    public static AsmRealReg get(String name) {
        return regMap.get(name);
    }

    public AsmRealReg(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String toString() {
        return name;
    }
}

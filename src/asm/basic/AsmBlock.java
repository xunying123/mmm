package src.asm.basic;

import src.asm.instructions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class AsmBlock {
    public String name;
    public LinkedList<AsmInstruction> insts = new LinkedList<>();
    public LinkedList<AsmInstruction> phi = new LinkedList<>();
    public LinkedList<AsmInstruction> jj = new LinkedList<>();
    public LinkedList<AsmBlock> pred = new LinkedList<>();
    public LinkedList<AsmBlock> succ = new LinkedList<>();
    public HashSet<AsmReg> in = new HashSet<>();
    public HashSet<AsmReg> out = new HashSet<>();
    public HashSet<AsmReg> use = new HashSet<>();
    public HashSet<AsmReg> def = new HashSet<>();
    public int depth = 0;

    public AsmBlock(String na, int dd) {
        this.depth = dd;
        this.name = na;
    }

    public void add(AsmInstruction aa) {
        if (aa instanceof AsmJump || aa instanceof AsmBeq || aa instanceof AsmBr) jj.add(aa);
        else insts.add(aa);
    }

    @Override
    public String toString() {
        String ret = "";
        if (name != null) {
            ret += name;
            ret += ":\n";
        }
        for (AsmInstruction aa : insts) {
            ret += " " + aa + "\n";
        }
        return ret;
    }
}

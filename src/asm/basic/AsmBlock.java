package src.asm.basic;

import src.asm.instructions.AsmInstruction;

import java.util.LinkedList;

public class AsmBlock {
    public String name;
    public LinkedList<AsmInstruction> insts = new LinkedList<>();

    public AsmBlock(String na) {
        this.name=na;
    }

    public void add(AsmInstruction aa) {
        insts.add(aa);
    }

    @Override
    public String toString() {
        String ret = "";
        if(name!=null) {
            ret+=name;
            ret+=":\n";
        }
        for(AsmInstruction aa : insts) {
            ret+= " " + aa + "\n";
        }
        return ret;
    }
}

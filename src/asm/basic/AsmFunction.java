package src.asm.basic;

import java.util.ArrayList;

public class AsmFunction {
    public String name;
    public ArrayList<AsmBlock> block = new ArrayList<>();

    public int regCnt = 0;
    public int alloca = 4;
    public int paraU = 0;
    public int all = 0;

    public AsmFunction(String na) {
        this.name = na;
    }

    public void add(AsmBlock bb) {
        block.add(bb);
    }

    @Override
    public String toString() {
        String ret = "  .text\n"+"  .globl "+name+"\n";
        ret+=name+":\n";
        for(AsmBlock bb : block) ret +=bb;
        return ret;
    }
}

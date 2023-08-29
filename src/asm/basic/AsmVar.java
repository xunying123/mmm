package src.asm.basic;

import src.IR.basic.IRGlobalVar;
import src.IR.irtype.*;

public class AsmVar extends Global {
    int value;
    int size;

    public AsmVar(IRGlobalVar gg) {
        super(gg.name);
        if (gg.initValue instanceof IRIntConst) {
            value = (((IRIntConst) gg.initValue).value);
            size = 4;
        } else if (gg.initValue instanceof IRBoolConst) {
            value = ((IRBoolConst) gg.initValue).value ? 1 : 0;
            size = 1;
        } else if (gg.initValue instanceof IRNullConst) {
            value = 0;
            size = 4;
        }
    }

    @Override
    public String toString() {
        String ret = "";
        ret+=name+":\n";
        ret+=((size==4)?" .word " :" .byte ") + value + "\n";
        return ret;
    }
}

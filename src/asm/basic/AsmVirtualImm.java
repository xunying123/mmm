package src.asm.basic;

import src.IR.irtype.*;

public class AsmVirtualImm extends AsmReg{
    int value;

    public AsmVirtualImm(int val) {
        this.value=val;
    }

    public AsmVirtualImm(IRConst ii) {
        if (ii instanceof IRIntConst) {
            value = (((IRIntConst) ii).value);
        } else if (ii instanceof IRBoolConst) {
            value = ((IRBoolConst) ii).value ? 1 : 0;
        } else if (ii instanceof IRCondConst) {
            value = ((IRCondConst) ii).value ? 1 : 0;
        } else {
            value = 0;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

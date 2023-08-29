package src.IR.basic;

import src.IR.irtype.IRType;
import src.asm.basic.AsmReg;
import src.ast.BuiltIn;

public abstract class IRBasic implements BuiltIn {
    public IRType type;
    public AsmReg reg;

    public IRBasic(IRType tt) {
        this.type=tt;
    }

    public abstract String toString () ;

    public abstract String toStringT();
}

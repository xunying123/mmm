package src.IR.irtype;

import src.IR.basic.IRBasic;

public abstract class IRConst extends IRBasic {
    public IRConst(IRType tt) {
        super(tt);
    }

    public abstract boolean is0() ;

    public abstract boolean equals(IRConst other);
}

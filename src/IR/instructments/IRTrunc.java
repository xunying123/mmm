package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.irtype.IRType;

public class IRTrunc extends IRCast {
    public IRTrunc(IRBlock bb, IRRegister rr, IRBasic vv, IRType tt) {
        super(bb,vv,tt,rr);
    }

    @Override
    public String toString() {
        return dest+" = trunc "+value.toStringT()+" to "+type;
    }
}

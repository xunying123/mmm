package src.IR.instructments;

import src.IR.basic.IRBasic;
import src.IR.basic.IRBlock;
import src.IR.basic.IRRegister;
import src.IR.irtype.IRType;

public class IRBitcast extends IRCast{
    public IRBitcast(IRBlock bb, IRBasic val, IRType tt, IRRegister de) {
        super(bb,val,tt,de);
    }

    @Override
    public String toString() {
        return dest+" = bitcast "+value.toStringT()+" to "+type;
    }


}

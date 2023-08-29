package src.IR.basic;

import src.IR.irtype.IRPtr;
import src.IR.irtype.IRType;

public class IRGlobalVar extends IRRegister{
    public IRBasic initValue;

    public IRGlobalVar (String name , IRType tt) {
        super(name,new IRPtr(tt));
        --cnt;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public String toStringT() {
        return type+" "+toString();
    }
}

package src.IR.irtype;

import src.IR.basic.IRBasic;

public class IRVoid extends IRType{
    public IRVoid() {
       super("void",0);
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public IRBasic defaultValue() {
        return null;
    }
}

package src.IR.irtype;

import src.IR.basic.IRBasic;

public class IRInt extends IRType{
    public int length;

    public IRInt(int length_) {
        super("i"+(length_),length_/8);
        this.length=length_;
    }

    @Override
    public String toString() {
        return "i"+(length);
    }

    @Override
    public IRBasic defaultValue() {
        return irInt0;
    }
}

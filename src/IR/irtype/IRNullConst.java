package src.IR.irtype;

public class IRNullConst extends IRConst{
    public IRNullConst() {
        super(irNull);
    }

    public IRNullConst (IRType tt) {
        super(tt);
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public String toStringT() {
        return type==irNull?toString():type+" "+ this;
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRNullConst;
    }

    @Override
    public boolean is0() {
        return true;
    }

}

package src.IR.irtype;

public class IRVoidConst extends IRConst{
    public IRVoidConst() {
        super(irVoid);
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public String toStringT() {
        return toString();
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRVoidConst;
    }

    @Override
    public boolean is0() {
        return false;
    }
}

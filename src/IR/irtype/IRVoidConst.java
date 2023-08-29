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
}

package src.IR.irtype;

public class IRCondConst extends IRConst {
    public boolean value;

    public IRCondConst(boolean val) {
        super(irCond);
        this.value=val;
    }

    @Override
    public String toString() {
        return value?"true":"false";
    }

    @Override
    public String toStringT() {
        return "i1 "+ this;
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRCondConst && ((IRCondConst) other).value ==value;
    }

    @Override
    public boolean is0() {
        return !value;
    }
}

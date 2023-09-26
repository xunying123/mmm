package src.IR.irtype;

public class IRIntConst extends IRConst{
    public int value;

    public IRIntConst(int value) {
        super(irInt);
        this.value=value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String toStringT() {
        return "i32 " + this;
    }

    @Override
    public boolean is0() {
        return value==0;
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRIntConst && ((IRIntConst) other).value==value;
    }
}

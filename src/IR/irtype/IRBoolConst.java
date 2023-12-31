package src.IR.irtype;

public class IRBoolConst extends IRConst {
    public boolean value;

    public IRBoolConst(boolean va) {
        super(irBool);
        this.value = va;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

    @Override
    public String toStringT() {
        return "i8 " + this;
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRBoolConst && ((IRBoolConst) other).value == value;
    }

    @Override
    public boolean is0() {
        return !value;
    }
}

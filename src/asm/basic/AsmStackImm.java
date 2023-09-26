package src.asm.basic;

public class AsmStackImm extends AsmVirtualImm {
    public AsmFunction fun;
    public int off;
    public boolean nn = false;

    public AsmStackImm(AsmFunction ff, int of) {
        super(0);
        this.fun = ff;
        this.off = of;
    }

    public AsmStackImm(AsmFunction ff, int of, boolean n) {
        super(0);
        this.fun = ff;
        this.off = of;
        this.nn = n;
    }

    public void cal() {
        value = fun.paraU + fun.alloca + off+fun.spillU;
        if (nn) value = -value;
    }
}

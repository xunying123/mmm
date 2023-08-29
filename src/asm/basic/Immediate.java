package src.asm.basic;

public class Immediate extends AsmNode{
    int value;

    public Immediate(int vv) {
        this.value=vv;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

package src.asm.basic;

public class AsmVirtualReg extends AsmReg{
    public int id;
    public int index = -1;
    public static int cnt = 0;
    public int size;

    public AsmVirtualReg(int siz) {
        this.size=siz;
        id = cnt++;
    }
}

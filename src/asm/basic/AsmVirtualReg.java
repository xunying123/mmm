package src.asm.basic;

public class AsmVirtualReg extends AsmReg{
    public int id=-1;
    public int index = -1;
    public static int cnt = 0;
    public int size = 0;

    public AsmVirtualReg(int siz) {
        this.size=siz;
        id = cnt++;
    }
}

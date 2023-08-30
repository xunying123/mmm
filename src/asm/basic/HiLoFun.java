package src.asm.basic;

public class HiLoFun extends Immediate{
    public enum Type {hi,lo}
    public Type type;
    public String str;

    public HiLoFun(Type tt,String ss) {
        super(0);
        this.type=tt;
        this.str=ss;
    }

    @Override
    public String toString() {
        return type==Type.hi?"%hi("+str+")":"%lo("+str+")";
    }
}

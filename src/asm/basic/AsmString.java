package src.asm.basic;

public class AsmString extends Global{
    public String ss;

    public AsmString (String na,String s) {
        super(na);
        this.ss=s;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += name + ":\n";
        ret += " .string \"" + ss.replace("\\","\\\\").replace("\n","\\n").replace("\0","").replace("\t","\\t").replace("\"","\\\"")+"\"\n";
        return ret;
    }
}

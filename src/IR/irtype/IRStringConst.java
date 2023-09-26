package src.IR.irtype;

public class IRStringConst extends IRConst {
    public String value;
    public int num;
    public int size;
    public static int cnt;

    public IRStringConst(String ss) {
        super(new IRPtr(new IRArray(irBool, ss.length() + 1)));
        this.value = ss;
        this.size = ss.length() + 1;
        this.num = cnt++;
    }

    @Override
    public String toString() {
        return "@str." + (num);
    }

    @Override
    public String toStringT() {
        return "[" + (size) + " x i8]* " + this;
    }

    public String printS() {
        String rr = "";
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\n' -> rr += "\\0A";
                case '\"' -> rr += "\\22";
                case '\\' -> rr += "\\\\";
                default -> rr += c;
            }
        }
        return rr + "\\00";
    }

    @Override
    public boolean is0() {
        return false;
    }

    @Override
    public boolean equals(IRConst other) {
        return other instanceof IRStringConst && ((IRStringConst) other).value.equals(value);
    }
}

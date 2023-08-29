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
        return "@str." + String.valueOf(num);
    }

    @Override
    public String toStringT() {
        return "[" + String.valueOf(size) + " x i8]* " + toString();
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
}

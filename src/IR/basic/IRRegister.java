package src.IR.basic;

import src.IR.irtype.IRType;

public class IRRegister extends IRBasic{
    public String name;
    public int index=-1;
    public static int cnt=0;

    public IRRegister(String name, IRType tt) {
        super(tt);
        this.name=name;
    }

    @Override
    public String toString() {
        if(index==-1 && (name==null || !name.equals("retval"))) index=cnt++;
        return "%." + ((name!=null && name.equals("retval"))?name:String.valueOf(index));
    }

    @Override
    public String toStringT() {
        return type + " "+toString();
    }
}

package src.IR.irtype;

import src.IR.basic.IRBasic;

public class IRArray extends IRType{
    public IRType ptrFrom;
    public int cnt;

    public IRArray(IRType pp, int cc) {
        super("[" + String.valueOf(cc)+ " x "+pp.name+"]",pp.size*cc);
        this.ptrFrom=pp;
        this.cnt=cc;
    }

    @Override
    public String toString() {
        return "[" + String.valueOf(cnt)+ " x "+ptrFrom.toString()+"]";
    }

    @Override
    public IRBasic defaultValue() {
        return new IRNullConst(this);
    }
}

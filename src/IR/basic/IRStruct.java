package src.IR.basic;

import src.IR.irtype.IRNullConst;
import src.IR.irtype.IRType;

import java.util.ArrayList;
import java.util.HashMap;

public class IRStruct extends IRType {
    public ArrayList<IRType> member = new ArrayList<>();
    public HashMap<String, Integer> offset = new HashMap<>();
    public boolean con = false;

    public IRStruct(String name, int size) {
        super(name, size);
    }

    public void add(IRType tt, String nn) {
        member.add(tt);
        offset.put(nn, member.size() - 1);
    }

    public boolean hadMem(String cc) {
        return offset.containsKey(cc);
    }

    public IRType get(String cc) {
        return !offset.containsKey(cc) ? null : member.get(offset.get(cc));
    }

    public void size() {
        size = member.size() << 2;
    }

    @Override
    public String toString() {
        return "%struct." + name;
    }

    @Override
    public IRBasic defaultValue() {
        return new IRNullConst(this);
    }
}

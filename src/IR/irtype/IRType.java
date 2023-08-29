package src.IR.irtype;

import src.IR.basic.IRBasic;
import src.ast.BuiltIn;

public abstract class IRType implements BuiltIn {
    public String name;
    public int size;

    public IRType(String name_) {
        this.name=name_;
    }

    public IRType(String name_,int size_) {
        this.name=name_;
        this.size=size_;
    }

    public abstract String toString();

    public abstract IRBasic defaultValue();

}

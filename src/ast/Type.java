package src.ast;

public class Type {
    public String name;
    public int dim = 0;
    public boolean Class = false;

    public Type(String name_) {
        this.name = name_;
        if ((!name.equals("VOID")) && (!name.equals("INT")) && (!name.equals("NULL")) && (!name.equals("BOOL")) && (!name.equals("THIS")) && (!name.equals("STRING")))
            Class = true;
    }

    public Type(String name_, int dim_) {
        this.name = name_;
        this.dim = dim_;
    }

    public Type(Type type_) {
        this.name = type_.name;
        this.dim = type_.dim;
        this.Class = type_.Class;
    }

    public boolean isReference() {
        return dim>0 || Class;
    }
}

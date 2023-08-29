package src.ast;

public class Type {
    public String name;
    public int dim = 0;
    public boolean Class = false;

    public Type(String name_) {
        this.name = name_;
        if ((!name.equals("void")) && (!name.equals("int")) && (!name.equals("null")) && (!name.equals("bool")) && (!name.equals("this")) && (!name.equals("string")))
            Class = true;
    }

    public Type(String name_, int dim_) {
        this(name_);
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

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj==this) return true;
        if(!(obj instanceof Type)) return false;
        Type type_ = (Type) obj;
        if(this.dim !=type_.dim) return false;
        if(!this.name.equals(type_.name)) return false;
        return true;
    }
}

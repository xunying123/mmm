package src.semantic;

import src.IR.basic.IRRegister;
import src.ast.Type;
import src.ast.astnode.definition.ClassDefinition;
import src.ast.astnode.statement.baseloop;

import java.util.HashMap;

public class Scope {
    public HashMap<String, Type> members = new HashMap<>();
    public Scope parent = null;
    public boolean inLoop = false;
    public ClassDefinition inCla = null;
    public baseloop inLoo = null;
    public Type retu = null;
    public boolean isRE = false;

    public HashMap<String, IRRegister> IRVar = new HashMap<>();
    public Scope() {
    }


    public Scope(Scope par) {
        this.parent = par;
        this.inLoop = par.inLoop;
        this.inLoo=par.inLoo;
        this.inCla = par.inCla;
    }

    public Scope(Scope par, Type re) {
        this.parent = par;
        this.retu = re;
        this.inCla = par.inCla;
    }

    public Scope(Scope par,ClassDefinition cla) {
        this.parent=par;
        this.inCla=cla;
    }

    public Scope(Scope par,baseloop inin) {
        this(par);
        this.inLoop=true;
        this.inLoo=inin;
    }

    public Scope(Scope par,boolean inl) {
        this(par);
        this.inLoop=inl;
    }

    public Type getType(String name) {
        if (members.containsKey(name)) {
            return members.get(name);
        } else {
            return parent == null ? null : parent.getType(name);
        }
    }

    public void addMem(String name, Type t) {
        members.put(name, t);
    }

    public boolean hasMem(String name) {
        return members.containsKey(name);
    }

    public void addIRVar(String name,IRRegister re) {
        IRVar.put(name ,re);
    }

    public IRRegister getIRVar(String name) {
        if(IRVar.containsKey(name)) {
            return IRVar.get(name);
        } else {
            return parent==null?null:parent.getIRVar(name);
        }
    }
}

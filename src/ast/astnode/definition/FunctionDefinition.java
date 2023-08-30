package src.ast.astnode.definition;

import src.IR.basic.IRFunction;
import src.ast.Position;
import src.ast.Type;
import src.ast.Visitor;
import src.ast.astnode.*;
import src.ast.astnode.statement.Statements;

import java.util.ArrayList;

public class FunctionDefinition extends AstNode {
    public TypeNode returnType;
    public String Name;
    public String className = null;
    public ParameterNode para = null;
    public ArrayList<Statements> sta = new ArrayList<>();
    public IRFunction irFunc = null;

    public FunctionDefinition(Position pos, String name) {
        super(pos);
        this.Name = name;
    }

    public FunctionDefinition(Position pos_, String name, String Classname, Type t1, Type t2, int cc) {
        super(pos_);
        this.returnType = new TypeNode(pos_, t1.name, t1.dim);
        this.Name = name;
        this.className = Classname;
        if (t2 != null && cc > 0) {
            this.para = new ParameterNode(pos_, t2, cc);
        }
    }

    public FunctionDefinition(Position pos_, String name, Type t1, Type t2, int cc) {
        super(pos_);
        this.returnType = new TypeNode(pos_, t1.name, t1.dim);
        this.Name = name;
        if (t2 != null && cc > 0) {
            this.para = new ParameterNode(pos_, t2, cc);
        }
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

package src.semantic;

import src.ast.BuiltIn;
import src.ast.Type;
import src.ast.Visitor;
import src.ast.astnode.FileAnalyze;
import src.ast.astnode.ParameterNode;
import src.ast.astnode.TypeNode;
import src.ast.astnode.definition.*;
import src.ast.astnode.expression.*;
import src.ast.astnode.statement.*;

public class Checker implements Visitor, BuiltIn {
    public GlobalScope glo;
    public Scope current;

    public Checker(GlobalScope gg) {
        this.glo = gg;
        current = gg;
    }

    public void visit(TypeNode node) {
        switch (node.type.name) {
            case "int", "bool", "string", "void", "null", "this" -> {
            }
            default -> {
                if (glo.getCla(node.type.name) == null) {
                    throw new eError(node.pos, "undefined type" + node.type.name);
                }
            }
        }
    }

    public void visit(VariableDeclaration node) {
        node.dec.forEach(vv -> vv.accept(this));
    }

    public void visit(ParameterNode node) {
        node.list.forEach(vv -> vv.accept(this));
    }

    public void visit(Break node) {
        if (!current.inLoop) {
            throw new eError(node.pos, "continue no in loop");
        }
    }

    public void visit(Continue node) {
        if (!current.inLoop) {
            throw new eError(node.pos, "continue no in loop");
        }
    }

    public void visit(Expression node) {
        if (node.express != null) {
            node.express.accept(this);
        }
    }

    public void visit(For node) {
        current = new Scope(current, true);
        if (node.var != null) {
            node.var.accept(this);
        }
        if (node.exp1 != null) {
            node.exp1.accept(this);
        }
        if (node.loopExp != null) {
            node.loopExp.accept(this);
            if (!node.loopExp.type.equals(Bool)) {
                throw new eError(node.pos, "no bool");
            }
        }
        if (node.exp2 != null) {
            node.exp2.accept(this);
        }
        node.sta.forEach(vv -> vv.accept(this));
        current = current.parent;
    }

    public void visit(If node) {
        node.exp.accept(this);
        if (!node.exp.type.equals(Bool)) {
            throw new eError(node.pos, "no bool");
        }
        if (node.trueS != null) {
            current = new Scope(current);
            node.trueS.forEach(vv -> vv.accept(this));
            current = current.parent;
        }
        if (node.falseS != null) {
            current = new Scope(current);
            node.falseS.forEach(vv -> vv.accept(this));
            current = current.parent;
        }
    }

    public void visit(Return node) {
        for (var temp = current; temp != null; temp = temp.parent) {
            if (temp.retu != null) {
                if (node.exp == null) {
                    if (!temp.retu.equals(Void)) {
                        throw new eError(node.pos, "wrong return type");
                    }
                } else {
                    node.exp.accept(this);
                    if ((!temp.retu.equals(node.exp.type)) && ((!temp.retu.isReference()) || (!node.exp.type.equals(Null)))) {
                        throw new eError(node.pos, "wrong return type");
                    }
                }
                temp.isRE = true;
                return;
            }
        }
        throw new eError(node.pos, "no need return");
    }

    public void visit(While node) {
        node.loopExp.accept(this);
        if (!node.loopExp.type.equals(Bool)) {
            throw new eError(node.pos, "no bool");
        }
        current = new Scope(current, true);
        node.sta.forEach(vv -> vv.accept(this));
        current = current.parent;
    }

    public void visit(ArrayEx node) {
        node.array.accept(this);
        node.index.accept(this);
        if (node.array.type == null || node.index.type == null || (!node.index.type.equals(Int))) {
            throw new eError(node.pos, "wrong expression");
        }
        node.type = new Type(node.array.type.name, node.array.type.dim - 1);
        if (node.type.dim < 0) {
            throw new eError(node.pos, "mismatch");
        }
    }

    public void visit(BinaryEx node) {
        node.leftNode.accept(this);
        node.rightNode.accept(this);
        if (node.rightNode.type == null || node.leftNode.type == null) {
            throw new eError(node.pos, "wrong type");
        }
        if (node.leftNode.type.equals(Null) || node.rightNode.type.equals(Null)) {
            if ((node.op.equals("==")) || (node.op.equals("!=")) && ((node.leftNode.type.isReference()) || (node.rightNode.type.isReference()))) {
                node.type = Bool;
                return;
            } else if (!node.leftNode.type.equals(node.rightNode.type)) {
                throw new eError(node.pos, "wrong type");
            }
        }
        if (node.leftNode.type.equals(Void) || node.rightNode.type.equals(Void)) {
            throw new eError(node.pos, "wrong type");
        }
        if (!node.leftNode.type.equals(node.rightNode.type)) {
            System.out.print(node.leftNode.type.equals(node.rightNode.type));
            throw new eError(node.pos, "wrong type");
        }

        switch (node.op) {
            case "+", "<=", ">=", "<", ">" -> {
                if ((!node.leftNode.type.equals(Int)) && (!node.leftNode.type.equals(string))) {
                    throw new eError(node.pos, "wrong type");
                }
                node.type = node.op.equals("+") ? node.leftNode.type : Bool;
            }
            case "*", "/", "%", "-", ">>", "<<", "&", "^", "|" -> {
                if (!node.leftNode.type.equals(Int)) {
                    throw new eError(node.pos, "wrong type");
                }
                node.type = Int;
            }
            case "&&", "||" -> {
                if (!node.leftNode.type.equals(Bool)) {
                    throw new eError(node.pos, "wrong type");
                }
                node.type = Bool;
            }
            default -> node.type = Bool;

        }
    }

    public void visit(AssignEx node) {
        node.leftNode.accept(this);
        node.rightNode.accept(this);
        if (node.leftNode.type == null || node.rightNode.type == null) {
            throw new eError(node.pos, "wrong expression");
        }
        if (node.leftNode.type.equals(Void) || node.rightNode.type.equals(Void)) {
            throw new eError(node.pos, "wrong expression");
        }
        if ((!node.leftNode.type.equals(node.rightNode.type)) && ((!node.leftNode.type.isReference()) || (!node.rightNode.type.equals(Null)))) {
            throw new eError(node.pos, "mismatch");
        }
        node.type = node.leftNode.type;
        if (!node.leftNode.isLeft()) {
            throw new eError(node.pos, "no left value");
        }
    }

    public void visit(ObjectEx node) {
        node.obj.accept(this);
        if (node.obj.type == null) {
            throw new eError(node.pos, "wrong expression");
        }

        if (!node.obj.type.isReference() && !node.obj.type.equals(This) && !node.obj.type.equals(string)) {
            System.out.print(node.obj.type.name);
            System.out.print(node.obj.type.Class);
            throw new eError(node.pos, "mismatch");
        }
        var cla = node.obj.type.equals(This) ? current.inCla : glo.getCla(node.obj.type.name);
        if (node.obj.type.dim > 0) {
            if (cla == null) {
                throw new eError(node.pos, "mismatch");
            }
            if (node.objTo.equals("size")) {
                node.func = ArraySizeFunc;
            }
        } else {
            if (cla == null) {
                throw new eError(node.pos, "mismatch");
            }
            node.type = cla.getType(node.objTo);
            node.func = cla.getFunc(node.objTo);
        }
    }

    public void visit(FunctionCall node) {
        node.express.forEach(vv -> vv.accept(this));
    }

    public void visit(FunctionEx node) {
        node.functi.accept(this);
        if (node.functi.func == null) {
            throw new eError(node.pos, "function is not defined" + node.functi.ss);
        }
        var ff = node.functi.func;
        if (node.exps != null) {
            node.exps.accept(this);
            if (ff.para == null || ff.para.list.size() != node.exps.express.size()) {
                throw new eError(node.pos, "wrong parameter");
            }
            for (int i = 0; i < ff.para.list.size(); i++) {
                var par = ff.para.list.get(i);
                var pp = node.exps.express.get(i);
                if ((!par.type.type.equals(pp.type)) && ((!par.type.type.isReference()) || (!pp.type.equals(Null)))) {
                    throw new eError(node.pos, "wrong parameter");
                }
            }
        } else {
            if (ff.para != null) {
                throw new eError(node.pos, "no parameter" + ff.Name);
            }
        }
        node.type = ff.returnType.type;
    }

    public void visit(NewEXp node) {
        for (var ss : node.List) {
            ss.accept(this);
            if (ss.type == null || !ss.type.equals(Int)) {
                throw new eError(node.pos, "wrong expression");
            }
        }
        new TypeNode(node.pos, node.typeName).accept(this);
        node.type = new Type(node.typeName, node.dim);
    }

    public void visit(Untary node) {
        node.exp.accept(this);
        if (node.exp.type == null) {
            throw new eError(node.pos, "wrong expression");
        }
        if (node.op.equals("++") || node.op.equals("--")) {
            if (!node.exp.isLeft() || !node.exp.type.equals(Int)) {
                throw new eError(node.pos, "wrong left value");
            }
            node.type = Int;
        } else if (node.op.equals("!")) {
            if (!node.exp.type.equals(Bool)) {
                throw new eError(node.pos, "wrong bool");
            }
            node.type = Bool;
        } else {
            if (!node.exp.type.equals(Int)) {
                throw new eError(node.pos, "wrong bool");
            }
            node.type = Int;
        }
    }

    public void visit(leftEx node) {
        node.exp.accept(this);
        if (node.exp.type == null) {
            throw new eError(node.pos, "wrong expression");
        }
        if (!node.exp.isLeft() || !node.exp.type.equals(Int)) {
            throw new eError(node.pos, "wrong left value");
        }
        node.type = Int;
    }

    public void visit(BlockStatement node) {
        current = new Scope(current);
        node.state.forEach(vv -> vv.accept(this));
        current = current.parent;
    }

    public void visit(BasicEx node) {
        if (node.ss.equals("null")) {
            node.type = Null;
        } else if ((node.ss.equals("true")) || (node.ss.equals("false"))) {
            node.type = Bool;
        } else if (node.ss.matches("\".*\"")) {
            node.type = string;
        } else if (node.ss.equals("this")) {
            if (current.inCla == null) {
                throw new eError(node.pos, "not in a class");
            }
            node.type = new Type(current.inCla.name);
        } else {
            node.type = Int;
        }
    }

    public void visit(ternaryEx node) {
        node.exp1.accept(this);
        node.exp2.accept(this);
        node.exp3.accept(this);
        if (!(node.exp1.type.equals(Bool))) {
            throw new eError(node.pos, "no bool");
        }
        if (node.exp2.type == null || node.exp3.type == null) {
            throw new eError(node.pos, "wrong expression");
        }
        if (node.exp2.type.equals(node.exp3.type)) {
            node.type = node.exp2.type;
            return;
        }
        if (node.exp2.type.isReference() && node.exp3.type.equals(Null)) {
            node.type = node.exp2.type;
            return;
        }
        if (node.exp3.type.isReference() && node.exp2.type.equals(Null)) {
            node.type = node.exp3.type;
            return;
        }
        throw new eError(node.pos, "mismatch");
    }

    public void visit(Declaration node) {
        node.type.accept(this);
        if (node.iniVal != null) node.iniVal.accept(this);
        if (current.hasMem(node.name)) {
            throw new eError(node.pos, "repeated variable " + node.name);
        }
        current.addMem(node.name, node.type.type);
    }

    public void visit(FunctionDefinition node) {
        node.returnType.accept(this);
        current = new Scope(current, node.returnType.type);
        if (node.para != null) {
            node.para.accept(this);
        }
        node.sta.forEach(vv -> vv.accept(this));
        if ((!node.returnType.type.equals(Void)) && (!node.Name.equals("main")) && (!current.isRE)) {
            throw new eError(node.pos, "Function" + node.Name + "has no return ");
        }
        current = current.parent;
    }

    public void visit(Construction node) {
        current = new Scope(current, Void);
        node.block.accept(this);
        current = current.parent;
    }

    public void visit(ClassDefinition node) {
        current = new Scope(current, node);
        node.var.forEach(vv -> vv.accept(this));
        if (node.con != null) {
            if (node.name.equals(node.con.name)) {
                node.con.accept(this);
            } else {
                throw new eError(node.pos, "wrong construction");
            }
        }
        node.func.forEach(vv -> vv.accept(this));
        current = current.parent;
    }

    public void visit(VarEx node) {
        node.type = current.getType(node.ss);
        if (current.inCla != null && current.inCla.getFunc(node.ss) != null) {
            node.func = current.inCla.getFunc(node.ss);
        } else {
            node.func = glo.getFunc(node.ss);
        }
    }

    public void visit(FileAnalyze node) {
        FunctionDefinition mainF = glo.getFunc("main");
        if (mainF == null || (!mainF.returnType.type.equals(Int)) || (mainF.para != null)) {
            throw new eError(node.pos, "wrong main function");
        }
        for (var vv : node.allFile) {
            vv.accept(this);
        }
    }
}

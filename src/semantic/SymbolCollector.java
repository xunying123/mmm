package src.semantic;

import src.ast.Visitor;
import src.ast.astnode.FileAnalyze;
import src.ast.astnode.ParameterNode;
import src.ast.astnode.TypeNode;
import src.ast.astnode.definition.*;
import src.ast.astnode.expression.*;
import src.ast.astnode.statement.*;

public class SymbolCollector implements Visitor {
    public GlobalScope globalSco;

    public SymbolCollector(GlobalScope gg) {
        this.globalSco = gg;
    }

    public void visit(TypeNode node) {
    }

    public void visit(VariableDeclaration node) {
    }

    public void visit(ParameterNode node) {
    }

    public void visit(Break node) {
    }

    public void visit(Continue node) {
    }

    public void visit(Expression node) {
    }

    public void visit(For node) {
    }

    public void visit(If node) {
    }

    public void visit(Return node) {
    }

    public void visit(While node) {
    }

    public void visit(ArrayEx node) {
    }

    public void visit(BinaryEx node) {
    }

    public void visit(AssignEx node) {
    }

    public void visit(ObjectEx node) {
    }

    public void visit(FunctionCall node) {
    }

    public void visit(FunctionEx node) {
    }

    public void visit(NewEXp node) {
    }

    public void visit(Untary node) {
    }

    public void visit(leftEx node) {
    }

    public void visit(BlockStatement node) {
    }

    public void visit(BasicEx node) {
    }

    public void visit(ternaryEx node) {
    }

    public void visit(Declaration node) {
    }

    public void visit(FunctionDefinition node) {
        if(globalSco.getFunc(node.Name)!=null) {
            throw new eError(node.pos,"function "+node.Name + " has been defined");
        }
        if(globalSco.getCla(node.Name)!=null) {
            throw new eError(node.pos,"function "+node.Name + " has been defined as a class");
        }
        globalSco.addFunc(node.Name,node);
    }

    public void visit(Construction node) {
    }

    public void visit(ClassDefinition node) {
        if(globalSco.getCla(node.name)!=null) {
            throw new eError(node.pos,"class "+node.name + " has been defined ");
        }
        if(globalSco.getFunc(node.name)!=null) {
            throw new eError(node.pos," class "+node.name + " has been defined as a function");
        }
        globalSco.addCla(node.name,node);
        for(var fun : node.func) {
            if(node.funcMap.containsKey(fun.Name)) {
                throw new eError(fun.pos," class's function "+fun.Name + " has been defined as a function");
            }
            fun.className=node.name;
            node.funcMap.put(fun.Name,fun);
        }
        for(var vv :node.var){
            for (var vaa : vv.dec) {
                if(node.varMap.containsKey(vaa.name)) {
                    throw new eError(vaa.pos," class's variable "+vaa.name + " has been defined as a variable");
                }
                node.varMap.put(vaa.name,vaa);
            }
        }
    }

    public void visit(VarEx node) {
    }

    public void visit(FileAnalyze node) {
        node.allFile.forEach(vv -> vv.accept(this));
    }

}

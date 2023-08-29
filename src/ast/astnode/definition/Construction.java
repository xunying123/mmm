package src.ast.astnode.definition;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.AstNode;
import src.ast.astnode.TypeNode;
import src.ast.astnode.statement.BlockStatement;

public class Construction extends AstNode {
    public String name;
    public BlockStatement block;
    public FunctionDefinition func;

    public Construction(Position pos_,String name, BlockStatement blo) {
        super(pos_);
        this.name=name;
        this.block=blo;
    }

    public FunctionDefinition toFunc () {
        FunctionDefinition funcc = new FunctionDefinition(pos,name);
        funcc.returnType=new TypeNode(pos,"void");
        funcc.sta=block.state;
        return this.func=funcc;
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}


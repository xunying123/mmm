package src.ast.astnode;

import src.ast.Position;
import src.ast.Type;
import src.ast.Visitor;
import src.ast.astnode.definition.Declaration;

import java.util.ArrayList;

public class ParameterNode extends AstNode {
    public ArrayList<Declaration> list = new ArrayList<>();

    public ParameterNode(Position pos_) {
        super(pos_);
    }

    public ParameterNode(Position pos_, Type type_, int cnt) {
        super(pos_);
        for (int i = 0; i < cnt; i++) {
            list.add(new Declaration(pos,"para"+i,new TypeNode(pos,type_.name,type_.dim)));
        }
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

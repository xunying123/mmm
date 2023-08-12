package src.ast.astnode;

import src.ast.Position;
import src.ast.Visitor;

import java.util.ArrayList;

public class FileAnalyze extends AstNode{
    public ArrayList<AstNode> allFile = new ArrayList<>();

    public FileAnalyze(Position pos_) {
        super(pos_);
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

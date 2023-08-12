package src.ast.astnode.definition;

import src.ast.Position;
import src.ast.Visitor;
import src.ast.astnode.AstNode;
import src.ast.astnode.TypeNode;
import src.ast.astnode.expression.ExpressionNode;

public class Declaration extends AstNode {
    public String name;
    public ExpressionNode iniVal;
    public TypeNode type;

    public Declaration(Position pos_, String name_, TypeNode type_) {
        super(pos_);
        this.type=type_;
        this.name=name_;
        this.iniVal=null;
    }

    public Declaration(Position pos_, String name_, TypeNode type_, ExpressionNode exp) {
        super(pos_);
        this.type=type_;
        this.name=name_;
        this.iniVal=exp;
    }
    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

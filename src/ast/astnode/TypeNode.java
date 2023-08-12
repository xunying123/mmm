package src.ast.astnode;

import src.ast.Type;
import src.ast.Position;
import src.ast.Visitor;

public class TypeNode extends AstNode{
   public Type type;

    public TypeNode(Position pos_) {
        super(pos_);
    }

    public TypeNode(Position pos_,String name_) {
        super(pos_);
        switch (name_) {
            case "INT" -> {
                this.type = Int;
            }
            case "VOID" -> {
                this.type = Void;
            }
            case "STRING" -> {
                this.type = string;
            }
            case "BOOL" -> {
                this.type = Bool;
            }
            default -> this.type = new Type(name_);
        }
    }

    public TypeNode(Position pos_,String name_,int dim_) {
        super(pos_);
        if(dim_==0) {
            switch (name_) {
                case "INT" -> {
                    this.type = Int;
                }
                case "VOID" -> {
                    this.type = Void;
                }
                case "STRING" -> {
                    this.type = string;
                }
                case "BOOL" -> {
                    this.type = Bool;
                }
                default -> this.type = new Type(name_);
            }
        }else {
            this.type = new Type(name_,dim_);
        }
    }

    @Override
    public void accept(Visitor vis) {
        vis.visit(this);
    }
}

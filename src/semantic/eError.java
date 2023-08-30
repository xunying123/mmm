package src.semantic;

import src.ast.Position;

public class eError extends RuntimeException{
    public String warning;
    public Position pos;
    public String ss=null;

    public eError(Position pos_,String war) {
        this.pos=pos_;
        this.warning=war;
    }

    @Override
    public String toString() {
        return "Warning: " + warning + "  at" + pos.toString() + " " + ss;
    }
}

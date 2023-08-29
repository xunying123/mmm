package src.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Position {
    private final int line;
    private final int column;

    public Position(int line, int column) {
        this.column = column;
        this.line = line;
    }

    public Position(Token token) {
        this.line = token.getLine();
        this.column = token.getCharPositionInLine();
    }

    public Position(TerminalNode terminal) {
        this(terminal.getSymbol());
    }

    public Position(ParserRuleContext ctx) {
        this(ctx.getStart());
    }

    public String toString() {
        return line + ":" + column;
    }

}

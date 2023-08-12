package src;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import src.grammar.MxLexer;
import src.grammar.MxParser;
import src.ast.AstBuilder;
import src.ast.astnode.FileAnalyze;
import src.semantic.*;

import java.io.IOException;


public class Compiler {
    public static void main(String[] args) throws IOException {
        System.out.println("Semantic check");
        
        if (args.length == 0) {
            System.out.println("Usage: java Compiler <filename>");
            return;
        } else if (args[0].equals("-fsyntax-only")) {
            CharStream input = CharStreams.fromStream(System.in);
            MxLexer lexer = new MxLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            ParseTree parseTreeRoot = parser.fileAnalyze();
            AstBuilder astbuilder = new AstBuilder();
            FileAnalyze fileA = (FileAnalyze) astbuilder.visit(parseTreeRoot);
            GlobalScope globalScope = new GlobalScope();
            new SymbolCollector(globalScope).visit(fileA);
            new Checker(globalScope).visit(fileA);
            return;
        } else if (args[0].equals("-S")) {
            System.out.println("Generate assembly code");
            return;
        } else {
            System.out.println("Unknown option");
            return;
        }
    }
}

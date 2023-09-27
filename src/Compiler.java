package src;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import src.IR.basic.IRFileAnalyze;
import src.asm.basic.AsmFile;
import src.codegen.IRBuilder;
import src.codegen.InstSelector;
import src.grammar.MxLexer;
import src.grammar.MxParser;
import src.ast.AstBuilder;
import src.ast.astnode.FileAnalyze;
import src.optmize.*;
import src.semantic.*;
import java.io.IOException;
import java.io.FileOutputStream;


public class Compiler {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java Compiler <filename>");
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
            IRFileAnalyze irFile = new IRFileAnalyze();
            new IRBuilder(irFile,globalScope).visit(fileA);
     //       new GlobalTo(irFile).work();
            new CFG(irFile).work();
            new MemToReg(irFile).work();
            AsmFile asmFile = new AsmFile();
            new InstSelector(asmFile).visit(irFile);
            new PremAllocator(asmFile).work();
            new manStack(asmFile).work();
            new Merge(asmFile).work();
            FileOutputStream irOut = new FileOutputStream("output.ll");
            irOut.write(irFile.toString().getBytes());
            irOut.close();
            FileOutputStream out = new FileOutputStream("test.s");
            out.write(asmFile.toString().getBytes());
            out.close();
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
        } else if (args[0].equals("-S")) {
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
            IRFileAnalyze irFile = new IRFileAnalyze();
            new IRBuilder(irFile,globalScope).visit(fileA);
        //    new GlobalTo(irFile).work();
            new CFG(irFile).work();
            new MemToReg(irFile).work();
            AsmFile asmFile = new AsmFile();
            new InstSelector(asmFile).visit(irFile);
            new PremAllocator(asmFile).work();
            new manStack(asmFile).work();
            new Merge(asmFile).work();
            //new RegAllocator(asmFile).work();
            System.out.print(asmFile.toString());
        }
    }
}

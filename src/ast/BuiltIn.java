package src.ast;

import src.ast.astnode.definition.FunctionDefinition;

public interface BuiltIn {
    Type Void = new Type("VOID");
    Type Int = new Type("INT");
    Type Null = new Type("NULL");
    Type Bool = new Type("BOOL");
    Type string = new Type("STRING");
    Type This = new Type("THIS");

    FunctionDefinition PrintFunc = new FunctionDefinition(null, "print", Void, string, 1);
    FunctionDefinition PrintlnFunc = new FunctionDefinition(null, "println", Void, string, 1);
    FunctionDefinition PrintIntFunc = new FunctionDefinition(null, "printInt", Void, Int, 1);
    FunctionDefinition PrintlnIntFunc = new FunctionDefinition(null, "printlnInt", Void, Int, 1);
    FunctionDefinition getStringFunc = new FunctionDefinition(null, "getString", string, null, 0);
    FunctionDefinition getIntFunc = new FunctionDefinition(null, "getInt", Int, null, 0);
    FunctionDefinition toStringFunc = new FunctionDefinition(null, "toString", string, Int, 1);
    FunctionDefinition ArraySizeFunc = new FunctionDefinition(null,"size",Int,null,0);
    FunctionDefinition StringLengthFunc = new FunctionDefinition(null,"length",Int,null,0);
    FunctionDefinition substringFunc = new FunctionDefinition(null,"substring",string,Int,2);
    FunctionDefinition parseFunc = new FunctionDefinition(null,"parserInt",Int,null,0);
    FunctionDefinition ordFunc = new FunctionDefinition(null,"ord",Int,Int,1);


}

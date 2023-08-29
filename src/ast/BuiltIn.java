package src.ast;

import src.IR.irtype.*;
import src.ast.astnode.definition.FunctionDefinition;

public interface BuiltIn {
    Type Void = new Type("void");
    Type Int = new Type("int");
    Type Null = new Type("null");
    Type Bool = new Type("bool");
    Type string = new Type("string");
    Type This = new Type("this");

    FunctionDefinition PrintFunc = new FunctionDefinition(null, "print", Void, string, 1);
    FunctionDefinition PrintlnFunc = new FunctionDefinition(null, "println", Void, string, 1);
    FunctionDefinition PrintIntFunc = new FunctionDefinition(null, "printInt", Void, Int, 1);
    FunctionDefinition PrintlnIntFunc = new FunctionDefinition(null, "printlnInt", Void, Int, 1);
    FunctionDefinition getStringFunc = new FunctionDefinition(null, "getString", string, null, 0);
    FunctionDefinition getIntFunc = new FunctionDefinition(null, "getInt", Int, null, 0);
    FunctionDefinition toStringFunc = new FunctionDefinition(null, "toString", string, Int, 1);
    FunctionDefinition ArraySizeFunc = new FunctionDefinition(null,"size",Int,null,0);
    FunctionDefinition StringLengthFunc = new FunctionDefinition(null,"length","string",Int,null,0);
    FunctionDefinition substringFunc = new FunctionDefinition(null,"substring","string",string,Int,2);
    FunctionDefinition parseFunc = new FunctionDefinition(null,"parseInt","string",Int,null,0);
    FunctionDefinition ordFunc = new FunctionDefinition(null,"ord","string",Int,Int,1);

    IRType irInt = new IRInt(32);
    IRType irIntPtr = new IRPtr(irInt);
    IRType irVoid = new IRVoid();
    IRType irNull = new IRPtr(irVoid);
    IRType irBool = new IRInt(8);
    IRType irChar = new IRInt(8);
    IRType irCond = new IRInt(1);
    IRType irString = new IRPtr(irChar);

    IRVoidConst irVoidConst = new IRVoidConst();
    IRCondConst irTrue = new IRCondConst(true);
    IRCondConst irFalse = new IRCondConst(false);
    IRBoolConst irBoolTrue = new IRBoolConst(true);
    IRBoolConst irBoolFalse = new IRBoolConst(false);
    IRIntConst irInt0 = new IRIntConst(0);
    IRIntConst irInt1 = new IRIntConst(1);
    IRIntConst irInt4 = new IRIntConst(4);
    IRIntConst irInt01 = new IRIntConst(-1);

}

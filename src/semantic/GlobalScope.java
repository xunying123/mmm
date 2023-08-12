package src.semantic;

import src.ast.BuiltIn;
import src.ast.astnode.definition.ClassDefinition;
import src.ast.astnode.definition.FunctionDefinition;

import java.util.HashMap;

public class GlobalScope extends Scope implements BuiltIn {
    public HashMap<String, FunctionDefinition> funcMem = new HashMap<>();
    public HashMap<String, ClassDefinition> classMem = new HashMap<>();

    public GlobalScope() {
        funcMem.put("print", PrintFunc);
        funcMem.put("println", PrintlnFunc);
        funcMem.put("printInt", PrintIntFunc);
        funcMem.put("printlnInt", PrintlnIntFunc);
        funcMem.put("getString", getStringFunc);
        funcMem.put("getInt", getIntFunc);
        funcMem.put("toString", toStringFunc);
        ClassDefinition str = new ClassDefinition(null, "string");
        str.funcMap.put("length", StringLengthFunc);
        str.funcMap.put("substring", substringFunc);
        str.funcMap.put("parseInt", parseFunc);
        str.funcMap.put("ord", ordFunc);
        classMem.put("string", str);
        classMem.put("int", new ClassDefinition(null, "int"));
        classMem.put("bool", new ClassDefinition(null, "bool"));
    }

    public void addFunc(String ss, FunctionDefinition ff) {
        funcMem.put(ss, ff);
    }

    public void addCla(String ss, ClassDefinition cc) {
        classMem.put(ss, cc);
    }

    public FunctionDefinition getFunc(String ss) {
        return funcMem.get(ss);
    }

    public ClassDefinition getCla(String ss) {
        return classMem.get(ss);
    }

}

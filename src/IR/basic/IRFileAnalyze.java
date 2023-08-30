package src.IR.basic;

import src.IR.instructments.IRJump;
import src.IR.instructments.IRRet;
import src.IR.irtype.IRPtr;
import src.IR.irtype.IRStringConst;
import src.ast.BuiltIn;

import java.util.ArrayList;
import java.util.HashMap;

public class IRFileAnalyze implements BuiltIn {
    public ArrayList<IRFunction> fuc = new ArrayList<>();
    public ArrayList<IRStruct> str = new ArrayList<>();
    public ArrayList<IRGlobalVar> var = new ArrayList<>();

    public HashMap<String, IRStringConst> string = new HashMap<>();
    public IRFunction initFunc = new IRFunction("Mx_global_init", irVoid);
    public IRFunction mainFunc;
    public IRBlock initBlock = new IRBlock(initFunc, "entry_");

    public IRFileAnalyze() {
        initFunc.add(initBlock);
        initFunc.exit = new IRBlock(initFunc, "return_");
        initBlock.ter = new IRJump(initBlock, initFunc.exit);
        initFunc.exit.ter = new IRRet(initFunc.exit, irVoidConst);
    }

    public IRStringConst addString(String str) {
        String vv = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\') {
                i++;
                switch (str.charAt(i)) {
                    case 'n' -> vv += '\n';
                    case '\"' -> vv += '\"';
                    default -> vv += '\\';
                }
            } else vv += c;
        }
        if (!string.containsKey(vv)) string.put(vv, new IRStringConst(vv));
        return string.get(vv);
    }

    @Override
    public String toString() {
        String ret = "";
        for (IRStruct ss : str) {
            ret += ss + " = type {";
            for (int i = 0; i < ss.member.size(); i++) {
                ret += ss.member.get(i);
                if (i != ss.member.size() - 1) ret += ", ";
            }
            ret += "}\n";
        }
        for (IRStringConst ss : string.values()) {
            ret += "@str." + String.valueOf(ss.num) + " = private unnamed_addr constant [" + String.valueOf(ss.value.length() + 1) + " x i8] c\"" + ss.printS() + "\"\n";
        }
        for (IRGlobalVar gg : var) {
            ret += gg + " = dso_local global " + ((IRPtr) gg.type).PointTo() + " " + gg.initValue + "\n";
        }
        ret += "\ndeclare dso_local i8* @malloc(i32)\n";
        ret += "declare dso_local i8* @strcpy(i8*, i8*)\n";
        ret += "declare dso_local i8* @strcat(i8*, i8*)\n";
        ret += "declare dso_local i32 @strlen(i8*)\n";
        ret += "declare void @print(i8*)\n";
        ret += "declare void @println(i8*)\n";
        ret += "declare void @printInt(i32)\n";
        ret += "declare void @printlnInt(i32)\n";
        ret += "declare i8* @getString()\n";
        ret += "declare i32 @getInt()\n";
        ret += "declare i8* @toString(i32)\n";
        ret += "declare i8* @__mx_substring(i8*, i32, i32)\n";
        ret += "declare i32 @__mx_parseInt(i8*)\n";
        ret += "declare i32 @__mx_ord(i8*, i32)\n";
        ret += "declare i8 @__mx_strlt(i8*, i8*)\n";
        ret += "declare i8 @__mx_strle(i8*, i8*)\n";
        ret += "declare i8 @__mx_strgt(i8*, i8*)\n";
        ret += "declare i8 @__mx_strge(i8*, i8*)\n";
        ret += "declare i8 @__mx_streq(i8*, i8*)\n";
        ret += "declare i8 @__mx_strneq(i8*, i8*)\n\n";
        if(initFunc !=null) {
            ret+=initFunc+"\n";
        }
        for (IRFunction ff : fuc) {
            ret += ff + "\n";
        }
        return ret;
    }
}

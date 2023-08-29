package src.asm.basic;

import java.util.ArrayList;

public class AsmFile {
    public ArrayList<AsmVar> var = new ArrayList<>();
    public ArrayList<AsmString> str = new ArrayList<>();
    public ArrayList<AsmFunction> fun = new ArrayList<>();

    @Override
    public String toString() {
        String ret = "";
        if(!var.isEmpty()) {
            ret+="  .section .data\n";
        }
        for(AsmVar vv : var) {
            ret +=vv;
        }
        if(!str.isEmpty()) {
            ret+="  .section .rodata\n";
        }
        for(AsmString ss : str) {
            ret +=ss;
        }
        for(AsmFunction ff : fun) {
            ret +=ff;
        }
        return ret;
    }
}

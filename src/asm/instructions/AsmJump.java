package src.asm.instructions;

import src.asm.basic.AsmBlock;

public class AsmJump extends AsmInstruction{
    public AsmBlock to;

    public AsmJump(AsmBlock tt) {
        this.to=tt;
    }

    @Override
    public String toString() {
        return "j "+to.name;
    }
}

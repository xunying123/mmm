package src.asm.instructions;

public class AsmCall extends AsmInstruction{
    String name;

    public AsmCall(String nn) {
        this.name=nn;
    }

    @Override
    public String toString() {
        return "call "+name;
    }
}

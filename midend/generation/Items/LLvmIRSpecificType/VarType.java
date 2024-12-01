package midend.generation.Items.LLvmIRSpecificType;

import midend.generation.Items.LLvmIRType;

public class VarType extends LLvmIRType {

    private int bits;

    public VarType(int i) {
        this.bits = i;
    }

    public int getBits() {
        return bits;
    }

    @Override
    public String toString() {
        return switch (bits) {
            case 1 -> "i1";
            case 8 -> "i8";
            case 32 -> "i32";
            default -> "void";
        };
    }
}

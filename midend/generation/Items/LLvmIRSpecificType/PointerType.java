package midend.generation.Items.LLvmIRSpecificType;

import midend.generation.Items.LLvmIRType;

public class PointerType extends LLvmIRType{

    public LLvmIRType pointTo;

    public PointerType(LLvmIRType type) {
        this.pointTo = type;
    }

    public LLvmIRType getPointTo() {
        return pointTo;
    }

    @Override
    public String toString() {
        return pointTo.toString() + "*";
    }
}

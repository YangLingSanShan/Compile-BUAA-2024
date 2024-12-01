package midend.generation.Values.Instruction.IOInstruction;

import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;

public class IoStreamInstruction extends Instruction {
    public IoStreamInstruction(LLvmIRType type, String name, String instrType) {
        super(type, name, instrType);
    }
}

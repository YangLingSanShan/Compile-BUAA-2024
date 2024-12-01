package midend.generation.Values.Instruction.BasicInstruction;

import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;

import java.util.ArrayList;

public class PhiInstruction extends Instruction {
    private final ArrayList<BasicBlock> indBasicBlock;

    public PhiInstruction(String name, ArrayList<BasicBlock> indBasicBlock, int... cnt) {
        super(new VarType(32), name, "phi");
        this.indBasicBlock = new ArrayList<>(indBasicBlock);
        int size = (cnt.length == 0) ? this.indBasicBlock.size() : cnt[0];
        addOperand(null, size);
        while (this.indBasicBlock.size() < size) {
            this.indBasicBlock.add(null);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ").append(instructiontype).append(" ").append(type).append(" ");

        for (int i = 0; i < operands.size(); i++) {
            sb.append("[ ").append(operands.get(i).getName());
            if (i < indBasicBlock.size()) {
                sb.append(", %").append(indBasicBlock.get(i).getName());
            }
            sb.append(" ]");
            if (i != operands.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}

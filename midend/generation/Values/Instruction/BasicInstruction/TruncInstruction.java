package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Lbu;
import backend.AssembleCodes.Codes.Specific.Li;
import backend.AssembleCodes.Codes.Specific.Sb;
import backend.AssembleCodes.Codes.Specific.Sw;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.Value;

public class TruncInstruction extends Instruction {

    private final LLvmIRType target;

    public TruncInstruction(String name, String instructionType, Value val, LLvmIRType target) {
        super(target, name, instructionType);
        this.target = target;
        addOperand(val);
    }

    @Override
    public String toString() {
        if (operands.isEmpty()) {
            return name + " = trunc invalid operand to " + target;
        }

        Value operand = operands.get(0);
        return String.format("%s = trunc %s %s to %s",
                name, operand.getType(), operand.getName(), target);
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        if (operands.get(0).getType().isInt32() && target.isInt8()) {
            if (operands.get(0) instanceof Constant constant) {
                int currentOffset = AssembleRegisterController.moveValueOffset(this);
                new Li(Register.K0, constant.getValue());
                new Sb(Register.SP, Register.K0, currentOffset);
            } else {
                int offset = AssembleRegisterController.moveValueOffset(this);
                Register target = AssembleRegisterController.getInstance().getRegister(this);
                new Lbu(Register.SP, (target == null) ? Register.K0 : target, offset + 4);
                new Sb(Register.SP, (target == null) ? Register.K0 : target, offset);
            }
        }

    }
}

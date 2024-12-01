package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Lbu;
import backend.AssembleCodes.Codes.Specific.Li;
import backend.AssembleCodes.Codes.Specific.Sw;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.Value;

public class ZextInstruction extends Instruction {
    private final LLvmIRType target;

    public ZextInstruction(String name, String instructionType, Value val, LLvmIRType target) {
        super(target, name, instructionType);
        this.target = target;
        addOperand(val);
    }

    @Override
    public String toString() {
        if (operands.isEmpty()) {
            return name + " = zext invalid operand to " + target;
        }

        Value operand = operands.get(0);
        return String.format("%s = zext %s %s to %s",
                name, operand.getType(), operand.getName(), target);
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        // 如果是 i1 类型的零扩展，我们需要将其转换为 i32 类型
        if (operands.get(0).getType().isInt1() && target.isInt32()) {
            Register oriRegister = AssembleRegisterController.getInstance().getRegister(operands.get(0));
            // 如果oriReg有对应的寄存器，我们只需要在栈指针上记录对应的偏移量即可，否则我们需要重新申请空间
            if (oriRegister != null) {
                AssembleRegisterController.getInstance().allocReg(this, oriRegister);
            } else {
                AssembleController.addOffset(this, AssembleController.getOffset(operands.get(0)));
            }
        } else if (operands.get(0).getType().isInt8() && target.isInt32()) {
            if (operands.get(0) instanceof Constant constant) {
                int currentOffset = AssembleRegisterController.moveValueOffset(this);
                new Li(Register.K0, constant.getValue());
                new Sw(Register.SP, Register.K0, currentOffset);
            } else {
                AssembleController.addOffset(this, AssembleController.getOffset(operands.get(0)));
            }
        }
    }
}

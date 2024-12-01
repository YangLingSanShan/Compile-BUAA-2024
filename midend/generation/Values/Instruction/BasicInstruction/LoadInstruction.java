package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Lw;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

public class LoadInstruction extends Instruction {
    public LoadInstruction(String localVarName, Value pointer) {
        super(((PointerType) pointer.getType()).getPointTo(), localVarName, "load");
        addOperand(pointer);
    }

    @Override
    public String toString() {
        if (operands.isEmpty()) {
            return name + " = " + instructiontype + " " + type + ", invalid operand";
        }

        Value operand = operands.get(0);
        return String.format("%s = %s %s, %s %s",
                name, instructiontype, type, operand.getType(), operand.getName());
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        // 首先 load 指令的第一个操作数是一个指针，我们需要先读取指针的值
        Register pointerRegister = AssembleRegisterController.getInstance().getRegister(operands.get(0));
        pointerRegister = AssembleRegisterController.getInstance().loadPointerValue(operands.get(0), pointerRegister, Register.K0);
        // 在寄存器控制器中查找当前指令对应的寄存器，如果为null则使用K0寄存器
        Register target = AssembleRegisterController.getInstance().getRegister(this);
        new Lw(pointerRegister, (target == null) ? Register.K0 : target, 0);
        // 如果使用了默认寄存器，那么我们需要重新申请空间
        AssembleRegisterController.getInstance().reAllocRegister(this, (target == null) ? Register.K0 : target);
    }
}

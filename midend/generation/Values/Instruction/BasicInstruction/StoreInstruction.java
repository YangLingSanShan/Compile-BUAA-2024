package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Sw;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

public class StoreInstruction extends Instruction {

    public StoreInstruction(Value ans, Value res) {
        super(new VarType(0), "StoreInstrution", "store");
        addOperand(ans);
        addOperand(res);
    }

    @Override
    public String toString() {
        if (operands.size() < 2) {
            return instructiontype + " invalid operands"; // 或者返回其他适合的错误信息
        }

        Value firstOperand = operands.get(0);
        Value secondOperand = operands.get(1);

        return String.format("%s %s %s, %s %s",
                instructiontype, firstOperand.getType(), firstOperand.getName(),
                secondOperand.getType(), secondOperand.getName());
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();

        // 首先 store 指令的第二个操作数是一个指针，我们需要先读取指针的值
        Register toRegister = AssembleRegisterController.getInstance().getRegister(operands.get(1));
        toRegister = AssembleRegisterController.getInstance().loadPointerValue(operands.get(1), toRegister, Register.K1);
        // 首先在寄存器控制器中查找当前指令对应的寄存器，如果为null则使用K0寄存器

        Value value = operands.get(0);
        Register fromReg = AssembleRegisterController.getInstance().getRegister(value);
        fromReg = AssembleRegisterController.getInstance().loadVariableValue(value, fromReg, Register.K0);
        // 然后我们使用 sw 指令将数据写入内存
        new Sw(toRegister, fromReg, 0);
    }
}

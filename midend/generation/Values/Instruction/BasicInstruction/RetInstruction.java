package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Jr;
import backend.AssembleCodes.Codes.Specific.Move;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

public class RetInstruction extends Instruction {

    public RetInstruction(Value retValue) {
        super(new VarType(0), "RetInstr", "ret");
        if (retValue != null) {
            addOperand(retValue);
        }
    }

    public Value getRetValue() {
        return operands.get(0);
    }

    @Override
    public String toString() {
        if (operands.isEmpty()) {
            return instructiontype + " void";
        }

        Value retValue = operands.get(0);
        return String.format("%s %s %s", instructiontype, retValue.getType(), retValue.getName());
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        Value retValue = operands.isEmpty() ? null : operands.get(0);
        if (retValue != null) {
            Register retRegister = AssembleRegisterController.getInstance().getRegister(retValue);
            retRegister = AssembleRegisterController.getInstance().loadVariableValue(retValue, retRegister, Register.V0);
            // 如果本身有对应的寄存器，我们需要添加一个move指令将结果移动到V0中
            if (retRegister != Register.V0) {
                new Move(Register.V0, retRegister);
            }
        }
        new Jr(Register.RA);
    }


}

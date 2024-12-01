package midend.generation.Values.Instruction.IOInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Li;
import backend.AssembleCodes.Codes.Specific.Move;
import backend.AssembleCodes.Codes.Specific.Syscall;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.BasicInstruction.ZextInstruction;
import midend.generation.Values.Value;

public class PutCharInstruction extends IoStreamInstruction {
    public PutCharInstruction(Value target) {
        super(new VarType(0), "PutChDeclare", "io");
        addOperand(target);
    }

    public static String getDeclare() {
        return "declare void @putch(i32)";
    }

    @Override
    public String toString() {
        return "call void @putch(i32 " + operands.get(0).getName() + ")";
    }


    @Override
    public void generateAssemble() {
        super.generateAssemble();
        // 首先我们需要将整数加载到寄存器中
        Value value = operands.get(0);
        Register targetReg = AssembleRegisterController.getInstance().getRegister(value);
        targetReg = AssembleRegisterController.getInstance().loadVariableValue(value, targetReg, Register.A0);
        // 然后我们使用 syscall 指令输出整数
        if (targetReg != Register.A0) {
            new Move(Register.A0, targetReg);
        }
        new Li(Register.V0, 11);
        new Syscall();
    }
}

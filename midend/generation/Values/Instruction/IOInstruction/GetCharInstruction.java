package midend.generation.Values.Instruction.IOInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Li;
import backend.AssembleCodes.Codes.Specific.Move;
import backend.AssembleCodes.Codes.Specific.Syscall;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;

public class GetCharInstruction extends IoStreamInstruction {
    public GetCharInstruction(String name, String instrType) {
        super(new VarType(32), name, instrType);
    }

    public static String getDeclare() {
        return "declare i32 @getchar() ";
    }

    @Override
    public String toString() {
        return name + " = call i32 @getchar()";
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        new Li(Register.V0, 12);
        new Syscall();
        // 然后我们需要将读取到的整数加载到寄存器中
        Register reg = AssembleRegisterController.getInstance().getRegister(this);
        // 如果寄存器已经被分配了，那么我们就需要将其移动到 V0 中
        if (reg != null) {
            new Move(reg, Register.V0);
        } else {
            AssembleRegisterController.getInstance().reAllocRegister(this, Register.V0);
        }
    }
}

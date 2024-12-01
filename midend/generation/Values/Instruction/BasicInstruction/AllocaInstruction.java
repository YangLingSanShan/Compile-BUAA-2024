package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Addi;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.Instruction;

public class AllocaInstruction extends Instruction {

    private LLvmIRType type;

    public AllocaInstruction(LLvmIRType type, String name) {
        super(new PointerType(type), name, "alloca");
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s = %s %s", name, instructiontype, type != null ? type : "invalid type");
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        AssembleController.moveCurrentOffset((type.isArray()) ? (-4 * ((ArrayType) type).calSpaceTot()) : -4);
        Register register = AssembleRegisterController.getInstance().getRegister(this);
        if (register != null) {
            // 如果存在对应的寄存器，那么我们直接将地址赋值给这个寄存器即可
            new Addi(register, Register.SP, AssembleController.getCurrentOffset());
        } else {
            // 如果不存在对应的寄存器则重新分配一个寄存器
            // 最低地址保存
            new Addi(Register.K0, Register.SP, AssembleController.getCurrentOffset());
            AssembleRegisterController.getInstance().allocReg(this, Register.K0);
        }
    }
}

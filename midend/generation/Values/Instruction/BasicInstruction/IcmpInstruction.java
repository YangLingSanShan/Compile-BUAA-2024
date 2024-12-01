package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.*;
import backend.AssembleCodes.Register;
import io.Settings;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

public class IcmpInstruction extends Instruction {
    /**
     * LLVM IR 中的比较指令
     * 包含了 eq, ne, sgt, sge, slt, sle
     */
    public IcmpInstruction(String name, String instructionType, Value ans, Value res) {
        super(new VarType(1), name, instructionType);
        addOperand(ans);
        addOperand(res);
    }

    @Override
    public String toString() {
        if (operands.size() < 2) {
            return name + " = icmp " + instructiontype + " invalid operands";
        }

        Value firstOperand = operands.get(0);
        Value secondOperand = operands.get(1);
        return String.format("%s = icmp %s %s %s, %s",
                name, instructiontype, firstOperand.getType(), firstOperand.getName(), secondOperand.getName());
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        // 我们需要查找当前指令的操作数对应的寄存器，如果rs为null则使用K0寄存器,
        // 如果rt为null则使用K1寄存器，如果target为null则使用K0寄存器
        Register rs = AssembleRegisterController.getInstance().getRegister(operands.get(0));
        rs = AssembleRegisterController.getInstance().loadVariableValue(operands.get(0), rs, Register.K0);
        Register rt = AssembleRegisterController.getInstance().getRegister(operands.get(1));
        rt = AssembleRegisterController.getInstance().loadVariableValue(operands.get(1), rt, Register.K1);
        Register target = AssembleRegisterController.getInstance().getRegister(this);
        target = (target == null) ? Register.K0 : target;
        switch (instructiontype) {
            case "eq" -> new Seq(rs, rt, target);
            case "ne" -> new Sne(rs, rt, target);
            case "sgt" -> new Sgt(rs, rt, target);
            case "sge" -> new Sge(rs, rt, target);
            case "slt" -> new Slt(rs, rt, target);
            case "sle" -> new Sle(rs, rt, target);
            default -> Settings.getInstance().addErrorInfo("Unknown Cmp Instruction");
        }
        // 如果使用了默认寄存器需要重新申请空间
        AssembleRegisterController.getInstance().reAllocRegister(this, target);
    }
}

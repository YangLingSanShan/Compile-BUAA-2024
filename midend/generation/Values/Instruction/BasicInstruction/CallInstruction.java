package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.*;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import backend.AssembleCodes.Register;

import java.util.ArrayList;

public class CallInstruction extends Instruction {
    private final Function target;

    public CallInstruction(String name, Function targetFunc, ArrayList<Value> paramList) {
        super(targetFunc.getReturnType(), name, "call");
        addOperand(targetFunc);
        this.target = targetFunc;
        paramList.forEach(this::addOperand);
    }

    public Function getTarget() {
        return target;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type.isVoid()) {
            sb.append("call void ");
        } else {
            sb.append(name).append(" = call ").append(type).append(" ");
        }

        sb.append(operands.get(0).getName()).append("(");

        for (int i = 1; i < operands.size(); i++) {
            Value operand = operands.get(i);
            sb.append(operand.getType()).append(" ").append(operand.getName());
            if (i < operands.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        ArrayList<Register> allocatedRegs = AssembleRegisterController.getInstance().getAllocatedRegister();
        // 将已经分配了寄存器的部分进行压栈
        int registerOffset = 0;
        int currentOffset = AssembleController.getCurrentOffset();
        for (Register register : allocatedRegs) {
            registerOffset += 4;
            new Sw(Register.SP, register, currentOffset - registerOffset);
        }
        //最后将SP指针和RA指针也进行压栈
        new Sw(Register.SP, Register.SP, currentOffset - registerOffset - 4);
        new Sw(Register.SP, Register.RA, currentOffset - registerOffset - 8);
        //将实参的值压入被调用函数的堆栈或者寄存器中
        for (int paraNum = 1; paraNum < operands.size(); paraNum++) {
            Value parameter = operands.get(paraNum);
            // 如果参数在前3个中，我们直接将他们放入a1-a3寄存器中
            if (paraNum <= 3 && AssembleRegisterController.getInstance().getRegisterHashMap() != null) {
                AssembleRegisterController.getInstance().allocParameterReg(parameter, Register.regTransform(Register.A0.ordinal() + paraNum), currentOffset, allocatedRegs);
            } else {// 如果参数不在前3个中或寄存器控制器中没有对应寄存器，我们将其存入堆栈中
                AssembleRegisterController.getInstance().allocParameterMem(parameter, Register.K0, currentOffset, allocatedRegs, paraNum);
            }
        }
        // 将sp设置为被调用函数的栈底地址
        new Addi(Register.SP, Register.SP, currentOffset - registerOffset - 8);
        // 调用函数
        new Jal(operands.get(0).getName().substring(1));
        // 恢复
        new Lw(Register.SP, Register.RA, 0);
        new Lw(Register.SP, Register.SP, 4);
        for (int offset = 0; offset < allocatedRegs.size(); offset++) {
            new Lw(Register.SP, allocatedRegs.get(offset), currentOffset - ((offset + 1) * 4));
        }
        // 如果当前函数有返回值，则从v0中获取返回值
        if (AssembleRegisterController.getInstance().getRegister(this) == null) {
            AssembleRegisterController.getInstance().allocReg(this, Register.V0);
        } else {
            new Move(AssembleRegisterController.getInstance().getRegister(this), Register.V0);
        }
    }
}

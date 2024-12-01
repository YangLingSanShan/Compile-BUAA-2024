package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.*;
import backend.AssembleCodes.Register;
import io.Settings;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.Value;

public class CalcInstruction extends Instruction {
    public CalcInstruction(String name, String instructionType, Value op1, Value op2) {
        super(new VarType(32), name, instructionType);
        addOperand(op1);
        addOperand(op2);
    }

    @Override
    public String toString() {
        if (operands.size() < 2) {
            return name + " = " + instructiontype + " i32 invalid operands";
        }

        Value firstOperand = operands.get(0);
        Value secondOperand = operands.get(1);
        return String.format("%s = %s i32 %s, %s",
                name, instructiontype, firstOperand.getName(), secondOperand.getName());
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        Register target = AssembleRegisterController.getInstance().getRegister(this);
        target = (target == null) ? Register.K0 : target;

        Register rs = AssembleRegisterController.getInstance().getRegister(operands.get(0));
        rs = AssembleRegisterController.getInstance().loadVariableValue(operands.get(0), rs, Register.K0);

        Register rt = AssembleRegisterController.getInstance().getRegister(operands.get(1));
        rt = AssembleRegisterController.getInstance().loadVariableValue(operands.get(1), rt, Register.K1);
        switch (instructiontype) {
            case "add":
                new Addu(rs, rt, target);
                break;
            case "sub":
                new Subu(rs, rt, target);
                break;
            case "and":
                new And(rs, rt, target);
                break;
            case "or":
                new Or(rs, rt, target);
                break;
            case "mul":
                new Mult(rs, rt);
                new Mflo(target);
                break;
            case "sdiv":
                new Div(rs, rt);
                new Mflo(target);
                break;
            case "srem":
                new Div(rs,rt);
                new Mfhi(target);
                break;
            default:
                Settings.getInstance().addErrorInfo("Unknown instruction type");
        }
        // 如果之前没有为当前指令分配寄存器，那么我们需要重新分配一个寄存器
        AssembleRegisterController.getInstance().reAllocRegister(this, target);
    }
}


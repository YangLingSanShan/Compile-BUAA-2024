package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Codes.Specific.Bne;
import backend.AssembleCodes.Codes.Specific.J;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.Value;

public class BrInstruction extends Instruction {
    private BasicBlock thenBlock;
    private BasicBlock elseBlock;

    public BrInstruction(Value con, BasicBlock thenBlock, BasicBlock elseBlock) {
        super(new VarType(0), "BrInstr", "br");
        addOperand(con);
        this.thenBlock = thenBlock;
        addOperand(thenBlock);
        this.elseBlock = elseBlock;
        addOperand(elseBlock);
    }

    public void setThenBlock(BasicBlock thenBlock) {
        this.thenBlock = thenBlock;
        operands.set(1, thenBlock);
    }

    public void setElseBlock(BasicBlock elseBlock) {
        this.elseBlock = elseBlock;
        operands.set(2, elseBlock);
    }

    public BasicBlock getThenBlock() {
        return thenBlock;
    }

    public BasicBlock getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return instructiontype + " i1 " + operands.get(0).getName() + ", label %" +
                operands.get(1).getName() + ", label %" + operands.get(2).getName();
    }

    @Override
    public void generateAssemble() {
        super.generateAssemble();
        // 首先在寄存器控制器中查找当前指令对应的寄存器，如果为null则使用K0寄存器
        Register reg = AssembleRegisterController.getInstance().getRegister(operands.get(0));
        reg = AssembleRegisterController.getInstance().loadRegisterValue(operands.get(0), Register.K0, reg);
        // 判断con是否不等于0，如果不等于0则跳转到thenBlock，否则跳转到elseBlock
        new Bne(reg, Register.ZERO, operands.get(1).getName());
        new J(operands.get(2).getName());
    }
}

package midend.generation.Values.Instruction.BasicInstruction;

import backend.AssembleCodes.Codes.Specific.J;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;

public class JumpInstruction extends Instruction {
    private BasicBlock target;
//    private boolean isAssemblerReduce;

    public JumpInstruction(BasicBlock target) {
        super(new VarType(0), "JumpInstr", "jump");
        addOperand(target);
        this.target = target;
//        this.isAssemblerReduce = false;
    }

    @Override
    public String toString() {
        return "br label %" + operands.get(0).getName();
    }

    @Override
    public void generateAssemble() {
         super.generateAssemble();
        // 如果当前指令需要被优化，则不生成汇编代码
        //        if (isAssemblerReduce) {
        //            return;
        //        }
        // 跳转指令的目标基本块
        new J(operands.get(0).getName());
    }

}

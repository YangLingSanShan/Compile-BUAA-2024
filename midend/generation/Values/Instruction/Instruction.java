package midend.generation.Values.Instruction;

import backend.AssembleCodes.Others.Comment;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.BasicInstruction.CallInstruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.User;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.optimizer.FunctionClone;
import midend.optimizer.Optimizer;
import midend.symplifyMethod.FunctionInlineUnit;

public class Instruction extends User {

    protected String instructiontype;
    private BasicBlock belongBasicBlock;

    public Instruction(LLvmIRType type, String name, String instructionType) {
        super(type, name);
        this.instructiontype = instructionType;

        //TODO: 优化
        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addInstruction(this);
        }
    }

    public Value copy(FunctionClone functionClone) {    //TODO: 内部全部实现
        return null;
    }

    public void setBelongBasicBlock(BasicBlock currentBasicBlock) {
        this.belongBasicBlock = currentBasicBlock;
    }

    public BasicBlock getBelongBasicBlock() {
        return belongBasicBlock;
    }

    @Override
    public void generateAssemble() {
        new Comment(this.toString());
    }

    public void buildFuncCallGraph() {
        if (this instanceof CallInstruction callInstr) {
            Function response = callInstr.getTarget();
            Function curFunc = this.getBelongBasicBlock().getBelongFunction();
            if (!FunctionInlineUnit.getCaller(curFunc).contains(response)) {
                FunctionInlineUnit.addCaller(curFunc, response);
            }
            if (!FunctionInlineUnit.getResponse(response).contains(curFunc)) {
                FunctionInlineUnit.addResponse(response, curFunc);
            }
        }
    }
}

package midend.generation.Values.Instruction;

import backend.AssembleCodes.Others.Comment;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.BasicInstruction.*;
import midend.generation.Values.Instruction.IOInstruction.GetCharInstruction;
import midend.generation.Values.Instruction.IOInstruction.GetIntInstruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.SubModule.User;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;
import midend.generation.Values.Value;
import midend.optimizer.FunctionClone;
import midend.optimizer.Optimizer;
import midend.symplifyMethod.FunctionInlineUnit;
import midend.symplifyMethod.Mem2RegUnit;

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

    public void insertPhiProcess() {
        Mem2RegUnit.reConfig(this);
        Mem2RegUnit.insertPhiInstruction();
    }

    public void addPhiToUse() {
        if (this instanceof PhiInstruction phiInstr) {
            for (Value operand : phiInstr.getOperands()) {
                if (operand instanceof Instruction || operand instanceof GlobalVar || operand instanceof Parameter) {
                    this.getBelongBasicBlock().getUseBasicBlockHashSet().add(operand);
                }
            }
        }
    }

    public void genUseDefAnalysis() {
        for (Value operand : this.getOperands()) {
            if (!this.getBelongBasicBlock().getDefBasicBlockHashSet().contains(operand) &&
                    (operand instanceof Instruction || operand instanceof GlobalVar || operand instanceof Parameter)) {
                this.getBelongBasicBlock().getUseBasicBlockHashSet().add(operand);
            }
        }
        if (!this.getBelongBasicBlock().getUseBasicBlockHashSet().contains(this) && this.isValid()) {
            this.getBelongBasicBlock().getDefBasicBlockHashSet().add(this);
        }
    }

    public boolean isValid() {
        boolean valid = this instanceof AllocaInstruction || this instanceof CalcInstruction || (this instanceof CallInstruction callInstr && !callInstr.getType().isVoid()) ||
                this instanceof GetIntInstruction || this instanceof GetCharInstruction;
        return valid || this instanceof PhiInstruction ||
                this instanceof GetEleInstruction || this instanceof IcmpInstruction ||
                this instanceof LoadInstruction || this instanceof ZextInstruction || this instanceof TruncInstruction;
    }
}

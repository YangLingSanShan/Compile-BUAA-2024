package midend.generation.Values.SubModule;

import midend.Units.BlockOptimizer;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.PointerType;
import midend.generation.Items.LLvmIRSpecificType.StructType;
import midend.generation.Values.Instruction.BasicInstruction.AllocaInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.optimizer.LoopVal;
import midend.optimizer.Optimizer;
import backend.AssembleCodes.Others.Label;
import midend.symplifyMethod.Mem2RegUnit;
import midend.optimizer.ControlFlowGraph;
import midend.optimizer.DominatorTree;
import midend.optimizer.LivenessAnalysisController;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.HashSet;


public class BasicBlock extends Value {
    private ArrayList<Instruction> instructionArrayList;
    private Function belongFunc;
    private boolean exist;
    private LoopVal loopVal;

    public BasicBlock(String name) {
        super(new StructType("basicBlock"), name);
        this.instructionArrayList = new ArrayList<>();
        //TODO: 优化
        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addBasicBlock(this);
        }
        this.exist = true;
    }

    public boolean setDeleted(boolean exist) {
        this.exist = !exist;
        return true;
    }

    public boolean isEmpty() {
        return this.instructionArrayList.isEmpty();
    }

    public Instruction getPreInstruction() {
        return instructionArrayList.get(instructionArrayList.size() - 1);
    }

    public void addInstruction(Instruction instr, Integer... idx) {
        if (idx.length == 1) {
            instructionArrayList.add(idx[0], instr);
        } else {
            instructionArrayList.add(instr);
        }
        instr.setBelongBasicBlock(this);
    }

    public void setBelongFunc(Function belongFunc) {
        this.belongFunc = belongFunc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n\t");
        for (int i = 0; i < instructionArrayList.size(); i++) {
            sb.append(instructionArrayList.get(i).toString());
            if (i != instructionArrayList.size() - 1) {
                sb.append("\n\t");
            }
        }
        return sb.toString();
    }

    @Override
    public void generateAssemble() {
        new Label(name);
        // instructionArrayList.forEach(Instruction::generateAssemble);
        for (Instruction instruction : instructionArrayList) {
            instruction.generateAssemble();
        }
    }


    public Function getBelongFunction() {
        return belongFunc;
    }

    public void buildFuncCallGraph() {
        instructionArrayList.forEach(Instruction::buildFuncCallGraph);
    }

    public void insertInstruction(Integer index, Instruction phiInstr) {
        instructionArrayList.add(index, phiInstr);
        phiInstr.setBelongBasicBlock(this);
    }

    public ArrayList<Instruction> getInstructionArrayList() {
        return instructionArrayList;
    }

    public void simplifyBlock() {
        BlockOptimizer.deleteDuplicateBranch(this);
    }

    public void insertPhiProcess() {
        ArrayList<Instruction> copy = new ArrayList<>(instructionArrayList);
        for (Instruction instruction : copy) {
            if (instruction instanceof AllocaInstruction &&
                    (((PointerType) instruction.getType()).getPointTo().isInt32() || ((PointerType) instruction.getType()).getPointTo().isInt8())) {
                instruction.insertPhiProcess();
                Mem2RegUnit.dfsVarRename(Mem2RegUnit.getInitialBasicBlock());
            }
        }
    }

    public boolean isExist() {
        return exist;
    }

    public ArrayList<BasicBlock> getBlockIndBasicBlock() {
        return ControlFlowGraph.getBlockIndBasicBlock(this);
    }

    public ArrayList<BasicBlock> getBlockDominanceFrontier() {
        return DominatorTree.getBlockDominanceFrontier(this);
    }

    public void setInstrArrayList(ArrayList<Instruction> instructions) {
        this.instructionArrayList = instructions;
    }

    public Instruction getLastInstruction() {
        return instructionArrayList.get(instructionArrayList.size() - 1);
    }

    public ArrayList<BasicBlock> getBlockOutBasicBlock() {
        return ControlFlowGraph.getBlockOutBasicBlock(this);
    }

    public ArrayList<BasicBlock> getBlockDominateSet() {
        return DominatorTree.getBlockDominateSet(this);
    }

    public ArrayList<BasicBlock> getBlockDominateChildList() {
        return DominatorTree.getBlockDominateChildList(this);
    }

    public BasicBlock getBlockDominateParent() {
        return DominatorTree.getBlockDominateParent(this);
    }

    public HashSet<Value> getInBasicBlockHashSet() {
        return LivenessAnalysisController.getInBasicBlockHashSet(this);
    }

    public HashSet<Value> getDefBasicBlockHashSet() {
        return LivenessAnalysisController.getDefBasicBlockHashSet(this);
    }
    public HashSet<Value> getUseBasicBlockHashSet() {
        return LivenessAnalysisController.getUseBasicBlockHashSet(this);
    }

    public void analysisActiveness() {
        HashSet<Value> def = new HashSet<>();
        HashSet<Value> use = new HashSet<>();
        LivenessAnalysisController.addDefBlockHashSet(this, def);
        LivenessAnalysisController.addUseBlockHashSet(this, use);
        instructionArrayList.forEach(Instruction::addPhiToUse);
        instructionArrayList.forEach(Instruction::genUseDefAnalysis);
    }

    public Instruction removeAndReturnInstruction() {
        LLvmIRNameGenerator.sub(belongFunc);
        if (!instructionArrayList.isEmpty()) {
            instructionArrayList.remove(instructionArrayList.size() - 1);
        }
        return getLastInstruction();
    }
}

package midend.generation.Values.SubModule.UserPackage;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleRegisterController;
import backend.AssembleCodes.Register;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.StructType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.Value;
import midend.optimizer.Optimizer;
import midend.symplifyMethod.Mem2RegUnit;
import midend.optimizer.DominatorTree;
import midend.optimizer.ControlFlowGraph;
import midend.optimizer.LivenessAnalysisController;
import backend.AssembleCodes.Others.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Function extends Value {
    private final LLvmIRType returnType;
    private final ArrayList<BasicBlock> basicBlocks;
    private final ArrayList<Parameter> params;
    private HashMap<Value, Register> registerHashMap;


    public Function(String name, LLvmIRType returnType) {
        super(new StructType("function"), name);
        this.returnType = returnType;
        this.basicBlocks = new ArrayList<>();
        this.params = new ArrayList<>();
        this.registerHashMap = null;

        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addFunction(this);
        }
    }

    public LLvmIRType getReturnType() {
        return returnType;
    }

    public void addBasicBlock(BasicBlock basicBlock, int... idx) {
        if (idx.length == 1) {
            basicBlocks.add(idx[0], basicBlock);
        } else {
            basicBlocks.add(basicBlock);
        }
        basicBlock.setBelongFunc(this);
    }

    public void addParameter(Parameter parameter) {
        this.params.add(parameter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ").append(returnType.toString())
                .append(" ").append(name).append("(");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).toString());
            if (i != params.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n\n");
        for (int i = 0; i < basicBlocks.size(); i++) {
            sb.append(basicBlocks.get(i).toString());
            if (i != basicBlocks.size() - 1) {
                sb.append("\n\n");
            }
        }
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public void generateAssemble() {
        new Label(name.substring(1));
        AssembleController.resetFunction(this);
        for (int i = 0; i < Math.min(3, params.size()); i++) {
            AssembleRegisterController.getInstance().allocRegister(params.get(i), Register.regTransform(Register.A0.ordinal() + i + 1));
            AssembleRegisterController.getInstance().moveValueOffset(params.get(i));
        }
        for (int i = 3; i < params.size(); i++) {
            AssembleRegisterController.getInstance().moveValueOffset(params.get(i));
        }
//        if (BackEndOptimizerUnit.isBasicBlockSorted()) {
//            BasicBlockSortedUnit.sort(basicBlocks);
//        }
        basicBlocks.forEach(BasicBlock::generateAssemble);
    }

    public HashMap<Value, Register> getRegisterHashMap() {
        return registerHashMap;
    }

    public void buildFuncCallGraph() {
        basicBlocks.forEach(BasicBlock::buildFuncCallGraph);
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public void insertPhiProcess() {
        Mem2RegUnit.setInitialBasicBlock(basicBlocks.get(0));
        basicBlocks.forEach(BasicBlock::insertPhiProcess);
    }

    public void simplifyBlock() {
        basicBlocks.forEach(BasicBlock::simplifyBlock);
    }

    public void searchBlockDominateSet() {
        BasicBlock entry = basicBlocks.get(0);
        for (BasicBlock basicBlock : basicBlocks) {
            HashSet<BasicBlock> reachedSet = new HashSet<>();
            DominatorTree.dfsDominate(entry, basicBlock, reachedSet);
            ArrayList<BasicBlock> domList = new ArrayList<>();
            for (BasicBlock bb : basicBlocks) {
                if (!reachedSet.contains(bb)) {
                    domList.add(bb);
                }
            }
            DominatorTree.addBlockDominateSet(basicBlock, domList);
        }
    }

    /**
     * searchBlockDominanceFrontier 方法用于在该 Function 中的所有基本块中进行支配边搜索，
     * 主要用于支配树的构建
     * 求解DominanceFrontier是一个固定的算法,对所有的边进行遍历即可
     */
    public void searchBlockDominanceFrontier() {
        for (Map.Entry<BasicBlock, ArrayList<BasicBlock>> entry :
                ControlFlowGraph.getFunctionOutBasicBlock(this).entrySet()) {
            BasicBlock from = entry.getKey();
            ArrayList<BasicBlock> outBasicBlocks = entry.getValue();
            for (BasicBlock to : outBasicBlocks) {
                BasicBlock runner = from;
                while (!runner.getBlockDominateSet().contains(to)
                        || runner.equals(to)) {
                    DominatorTree.addBlockDominanceFrontierEdge(runner, to);
                    runner = runner.getBlockDominateParent();
                }
            }
        }
    }

    /**
     * searchBlockDominateTreeDepth 方法用于在该 Function 中的所有基本块中进行支配树深度搜索，
     * 主要用于GCM优化
     */
    public void searchBlockDominateTreeDepth() {
        DominatorTree.dfsDominateLevel(basicBlocks.get(0), 0);
    }

    public void analysisActiveness() {
        HashMap<BasicBlock, HashSet<Value>> inMap = new HashMap<>();
        HashMap<BasicBlock, HashSet<Value>> outMap = new HashMap<>();
        LivenessAnalysisController.addInFunctionHashMap(this, inMap);
        LivenessAnalysisController.addOutFunctionHashMap(this, outMap);
        for (BasicBlock basicBlock : basicBlocks) {
            LivenessAnalysisController.addInBlockHashSet(basicBlock, new HashSet<>());
            LivenessAnalysisController.addOutBlockHashSet(basicBlock, new HashSet<>());
        }
        basicBlocks.forEach(BasicBlock::analysisActiveness);
        LivenessAnalysisController.calculateInOut(this);
    }

}

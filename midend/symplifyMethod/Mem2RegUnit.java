package midend.symplifyMethod;

import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.BasicInstruction.LoadInstruction;
import midend.generation.Values.Instruction.BasicInstruction.PhiInstruction;
import midend.generation.Values.Instruction.BasicInstruction.StoreInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.optimizer.MidendOptimizer;
import midend.optimizer.Use;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Mem2RegUnit {

    private static BasicBlock initialBasicBlock;
    private static ArrayList<Instruction> useInstructions;
    private static ArrayList<Instruction> defInstructions;
    private static ArrayList<BasicBlock> useBasicBlocks;
    private static ArrayList<BasicBlock> defBasicBlocks;
    private static Stack<Value> stack;
    private static Instruction nowAllocaInstruction;

    public static void optimize(LLvmIRModule module) {
            module.simplifyBlock();
            Mem2RegUnit.init(module);
            module.getFunctions().forEach(Function::insertPhiProcess);
    }

    private static void init(LLvmIRModule module) {
        MidendOptimizer.build(module);
    }

    public static void setInitialBasicBlock(BasicBlock basicBlock) {
        initialBasicBlock = basicBlock;
    }

    public static BasicBlock getInitialBasicBlock() {
        return initialBasicBlock;
    }

    public static void reConfig(Instruction instruction) {
        Mem2RegUnit.useInstructions = new ArrayList<>();
        Mem2RegUnit.defInstructions = new ArrayList<>();
        Mem2RegUnit.useBasicBlocks = new ArrayList<>();
        Mem2RegUnit.defBasicBlocks = new ArrayList<>();
        Mem2RegUnit.stack = new Stack<>();
        Mem2RegUnit.nowAllocaInstruction = instruction;
        for (Use use : instruction.getUses()) {
            Instruction user = (Instruction) use.getUser();
            if (user instanceof LoadInstruction loadInstr && loadInstr.getBelongBasicBlock().isExist()) {
                useInstructions.add(loadInstr);
                if (!defBasicBlocks.contains(loadInstr.getBelongBasicBlock())) {
                    useBasicBlocks.add(loadInstr.getBelongBasicBlock());
                }
            } else if (user instanceof StoreInstruction storeInstr
                    && storeInstr.getBelongBasicBlock().isExist()) {
                defInstructions.add(storeInstr);
                if (!defBasicBlocks.contains(storeInstr.getBelongBasicBlock())) {
                    defBasicBlocks.add(storeInstr.getBelongBasicBlock());
                }
            }
        }
    }

    /**
     * dfsVarRename 方法用于深度优先搜索基本块
     * 并对基本块中的变量进行重命名
     * cnt 记录遍历presentBlock的过程中，stack的push次数
     * 该函数执行逻辑如下:
     * 1.在移除了非必要的Instr后，遍历presentBlock的后继集合，
     * 将最新的define（stack.peek）填充进每个后继
     * 块的第一个phi指令中(有可能某个后继块没有和当
     * 前alloc指令相关的phi，需要进行特判(符合条件
     * 的Phi应该在useInstrList中))
     * 2.对presentBlock支配的基本块使用dfsVarRename方法，实现DFS
     * 3.将该次dfs时压入stack的数据全部弹出
     */
    public static void dfsVarRename(BasicBlock presentBlock) {
        int cnt = removeUnnecessaryInstr(presentBlock);
        for (BasicBlock basicBlock : presentBlock.getBlockOutBasicBlock()) {
            Instruction instr = basicBlock.getInstructionArrayList().get(0);
            if (instr instanceof PhiInstruction phiInstr && useInstructions.contains(phiInstr)) {
                phiInstr.modifyValue(((stack.isEmpty()) ? new Constant(new VarType(32),"0", false) : stack.peek()), presentBlock);
            }
        }
        presentBlock.getBlockDominateChildList().forEach(Mem2RegUnit::dfsVarRename);
        for (int i = 0; i < cnt; i++) {
            stack.pop();
        }
    }

    private static int removeUnnecessaryInstr(BasicBlock presentBlock) {
        int instrNum = 0;
        Iterator<Instruction> iter = presentBlock.getInstructionArrayList().iterator();
        while (iter.hasNext()) {
            Instruction instr = iter.next();
            if (instr instanceof StoreInstruction storeInstr && defInstructions.contains(storeInstr)) {
                instrNum++;
                stack.push(storeInstr.getOperands().get(0));
                iter.remove();
                storeInstr.dropOperands();
            } else if (instr instanceof LoadInstruction loadInstr && useInstructions.contains(loadInstr)) {
                loadInstr.replaceAllUse(((stack.isEmpty()) ? new Constant(new VarType(32), "0", false) : stack.peek()));
                iter.remove();
            } else if (instr instanceof PhiInstruction phiInstr && defInstructions.contains(phiInstr)) {
                instrNum++;
                stack.push(phiInstr);
            } else if (instr.equals(Mem2RegUnit.nowAllocaInstruction)) {
                iter.remove();
                instr.dropOperands();
            }
        }
        return instrNum;
    }

    public static void insertPhiInstruction() {
        HashSet<BasicBlock> f = new HashSet<>();
        Stack<BasicBlock> w = new Stack<>();
        Mem2RegUnit.defBasicBlocks.forEach(w::push);
        while (!w.isEmpty()) {
            BasicBlock x = w.pop();
            for (BasicBlock y : x.getBlockDominanceFrontier()) {
                if (!f.contains(y)) {
                    f.add(y);
                    Instruction phiInstr = new PhiInstruction(LLvmIRNameGenerator.getLocalVarName(y.getBelongFunction()), y.getBlockIndBasicBlock());
                    y.insertInstruction(0, phiInstr);
                    useInstructions.add(phiInstr);
                    defInstructions.add(phiInstr);
                    if (!defBasicBlocks.contains(y)) {
                        w.push(y);
                    }
                }
            }
        }
    }
}

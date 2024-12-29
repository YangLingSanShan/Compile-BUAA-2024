package midend.optimizer;

import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DominatorTree {

    private static HashMap<Function, HashMap<BasicBlock, ArrayList<BasicBlock>>> dominateFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, ArrayList<BasicBlock>>> dominanceFrontierFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, BasicBlock>> parentFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, ArrayList<BasicBlock>>> childListFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, Integer>> dominanceTreeDepthHashMap = new HashMap<>();


    public static ArrayList<BasicBlock> getBlockDominanceFrontier(BasicBlock basicBlock) {
        return DominatorTree.getFunctionDominanceFrontier(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static HashMap<BasicBlock, ArrayList<BasicBlock>> getFunctionDominanceFrontier(Function function) {
        return DominatorTree.dominanceFrontierFunctionHashMap.get(function);
    }

    public static void build(LLvmIRModule module) {
        for (Function function : module.getFunctions()) {
            DominatorTree.addFunctionDominate(function, new HashMap<>());
            DominatorTree.addFunctionDominanceFrontier(function, new HashMap<>());
            DominatorTree.addFunctionDominateParent(function, new HashMap<>());
            DominatorTree.addFunctionDominateChildList(function, new HashMap<>());
            DominatorTree.addFunctionDominanceTreeDepth(function, new HashMap<>());
            for (BasicBlock basicBlock : function.getBasicBlocks()) {
                DominatorTree.addBlockDominateSet(basicBlock, new ArrayList<>());
                DominatorTree.addBlockDominanceFrontier(basicBlock, new ArrayList<>());
                DominatorTree.addBlockDominateParent(basicBlock, null);
                DominatorTree.addBlockDominateChildList(basicBlock, new ArrayList<>());
                DominatorTree.addBlockDominateTreeDepth(basicBlock, null);
            }
            function.searchBlockDominateSet();
            DominatorTree.buildDominateTree(function);
            function.searchBlockDominanceFrontier();
            function.searchBlockDominateTreeDepth();
        }
    }

    private static void buildDominateTree(Function function) {
        function.getBasicBlocks().forEach(basicBlock -> basicBlock.getBlockDominateSet().forEach(dominateBlock -> buildDoubleEdge(basicBlock, dominateBlock)));
    }

    private static void buildDoubleEdge(BasicBlock basicBlock, BasicBlock dominateBlock) {
        if (DominatorTree.isImmediateDominator(basicBlock, dominateBlock)) {
            addDoubleEdge(basicBlock, dominateBlock);
        }
    }

    private static boolean isImmediateDominator(BasicBlock basicBlock, BasicBlock dominateBlock) {
        boolean flag = basicBlock.getBlockDominateSet().contains(dominateBlock) &&
                !dominateBlock.equals(basicBlock);
        for (BasicBlock midBlock : basicBlock.getBlockDominateSet()) {
            if (!midBlock.equals(dominateBlock) && !midBlock.equals(basicBlock)
                    && midBlock.getBlockDominateSet().contains(dominateBlock)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static void addDoubleEdge(BasicBlock fromBlock, BasicBlock toBlock) {
        DominatorTree.addBlockDominateParent(toBlock, fromBlock);
        DominatorTree.addBlockDominateChild(fromBlock, toBlock);
    }

    public static void addBlockDominateChild(BasicBlock runner, BasicBlock to) {
        childListFunctionHashMap.computeIfAbsent(runner.getBelongFunction(), k -> new HashMap<>());
        DominatorTree.getFunctionDominateChildList(runner.getBelongFunction()).get(runner).add(to);
    }


    public static void addFunctionDominate(Function function, HashMap<BasicBlock, ArrayList<BasicBlock>> hashMap) {
        DominatorTree.dominateFunctionHashMap.put(function, hashMap);
    }

    public static void addFunctionDominanceFrontier(Function function, HashMap<BasicBlock, ArrayList<BasicBlock>> hashMap) {
        DominatorTree.dominanceFrontierFunctionHashMap.put(function, hashMap);
    }

    public static void addFunctionDominateParent(Function function, HashMap<BasicBlock, BasicBlock> hashMap) {
        DominatorTree.parentFunctionHashMap.put(function, hashMap);
    }

    public static void addFunctionDominateChildList(Function function, HashMap<BasicBlock, ArrayList<BasicBlock>> hashMap) {
        DominatorTree.childListFunctionHashMap.put(function, hashMap);
    }

    public static void addFunctionDominanceTreeDepth(Function function, HashMap<BasicBlock, Integer> hashMap) {
        DominatorTree.dominanceTreeDepthHashMap.put(function, hashMap);
    }

    public static void addBlockDominateSet(BasicBlock basicBlock, ArrayList<BasicBlock> domList) {
        dominateFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        DominatorTree.getFunctionDominate(basicBlock.getBelongFunction()).put(basicBlock, domList);
    }

    public static HashMap<BasicBlock, ArrayList<BasicBlock>> getFunctionDominate(Function function) {
        return DominatorTree.dominateFunctionHashMap.get(function);
    }

    public static void addBlockDominanceFrontier(BasicBlock basicBlock, ArrayList<BasicBlock> domList) {
        dominanceFrontierFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(),
                k -> new HashMap<>());
        DominatorTree.getFunctionDominanceFrontier(basicBlock.getBelongFunction())
                .put(basicBlock, domList);
    }

    public static void addBlockDominateParent(BasicBlock basicBlock, BasicBlock domParent) {
        parentFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(),
                k -> new HashMap<>());
        DominatorTree.getFunctionDominateParent(basicBlock.getBelongFunction())
                .put(basicBlock, domParent);
    }

    public static HashMap<BasicBlock, BasicBlock> getFunctionDominateParent(Function function) {
        return DominatorTree.parentFunctionHashMap.get(function);
    }

    public static void addBlockDominateChildList(BasicBlock basicBlock, ArrayList<BasicBlock> domList) {
        childListFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        DominatorTree.getFunctionDominateChildList(basicBlock.getBelongFunction()).put(basicBlock, domList);
    }

    public static HashMap<BasicBlock, ArrayList<BasicBlock>> getFunctionDominateChildList(Function function) {
        return DominatorTree.childListFunctionHashMap.get(function);
    }

    private static void addBlockDominateTreeDepth(BasicBlock basicBlock, Integer depth) {
        dominanceTreeDepthHashMap.computeIfAbsent(basicBlock.getBelongFunction(),
                k -> new HashMap<>());
        DominatorTree.getFunctionDominanceTreeDepth(basicBlock.getBelongFunction())
                .put(basicBlock, depth);
    }

    public static HashMap<BasicBlock, Integer> getFunctionDominanceTreeDepth(Function function) {
        return DominatorTree.dominanceTreeDepthHashMap.get(function);
    }

    public static void dfsDominate(BasicBlock entry, BasicBlock basicBlock, HashSet<BasicBlock> reachedSet) {
        if (entry.equals(basicBlock)) {
            return;
        }
        reachedSet.add(entry);
        for (BasicBlock child : entry.getBlockOutBasicBlock()) {
            if (!reachedSet.contains(child)) {
                DominatorTree.dfsDominate(child, basicBlock, reachedSet);
            }
        }
    }

    public static ArrayList<BasicBlock> getBlockDominateSet(BasicBlock basicBlock) {
        return DominatorTree.getFunctionDominate(basicBlock.getBelongFunction()).get(basicBlock);
    }

    /**
     * addBlockDominateFrontierEdge 方法用于向控制流图中添加基本块的支配边界集合
     */
    public static void addBlockDominanceFrontierEdge(BasicBlock runner, BasicBlock to) {
        dominanceFrontierFunctionHashMap.computeIfAbsent(runner.getBelongFunction(),
                k -> new HashMap<>());
        DominatorTree.getFunctionDominanceFrontier(runner.getBelongFunction())
                .get(runner).add(to);
    }

    /**
     * dfsDominateLevel 方法用于计算支配树的深度，
     * 该函数依托于深度优先搜索进行
     * 主要用于在GVN中计算支配树的深度
     */
    public static void dfsDominateLevel(BasicBlock basicBlock, Integer depth) {
        DominatorTree.addBlockDominateTreeDepth(basicBlock, depth);
        for (BasicBlock child : basicBlock.getBlockDominateChildList()) {
            DominatorTree.dfsDominateLevel(child, depth + 1);
        }
    }

    public static ArrayList<BasicBlock> getBlockDominateChildList(BasicBlock basicBlock) {
        return DominatorTree.getFunctionDominateChildList(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static BasicBlock getBlockDominateParent(BasicBlock basicBlock) {
        return DominatorTree.getFunctionDominateParent(basicBlock.getBelongFunction()).get(basicBlock);
    }
}

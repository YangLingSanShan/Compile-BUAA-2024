package midend.optimizer;

import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LivenessAnalysisController {
    private static HashMap<Function, HashMap<BasicBlock, HashSet<Value>>> inFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, HashSet<Value>>> outFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, HashSet<Value>>> useFunctionHashMap = new HashMap<>();
    private static HashMap<Function, HashMap<BasicBlock, HashSet<Value>>> defFunctionHashMap = new HashMap<>();

    public static void analysis(LLvmIRModule module) {
        module.getFunctions().forEach(Function::analysisActiveness);
    }


    /**
     * addInFunctionHashMap 方法用于向inFunctionHashMap中添加函数和哈希表
     */
    public static void addInFunctionHashMap(
            Function function, HashMap<BasicBlock, HashSet<Value>> hashMap) {
        inFunctionHashMap.put(function, hashMap);
    }

    /**
     * addOutFunctionHashMap 方法用于向outFunctionHashMap中添加函数和哈希表
     */
    public static void addOutFunctionHashMap(
            Function function, HashMap<BasicBlock, HashSet<Value>> hashMap) {
        outFunctionHashMap.put(function, hashMap);
    }

    /**
     * addInBlockHashSet 方法用于向inFunctionHashMap中添加基本块和哈希表
     */
    public static void addInBlockHashSet(BasicBlock basicBlock, HashSet<Value> hashSet) {
        inFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        LivenessAnalysisController.getInFunctionHashMap(basicBlock.getBelongFunction()).put(basicBlock, hashSet);
    }

    /**
     * addOutBlockHashSet 方法用于向outFunctionHashMap中添加基本块和哈希表
     */
    public static void addOutBlockHashSet(BasicBlock basicBlock, HashSet<Value> hashSet) {
        outFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        LivenessAnalysisController.getOutFunctionHashMap(basicBlock.getBelongFunction()).put(basicBlock, hashSet);
    }

    public static HashMap<BasicBlock, HashSet<Value>> getInFunctionHashMap(Function function) {
        return inFunctionHashMap.get(function);
    }

    public static HashMap<BasicBlock, HashSet<Value>> getOutFunctionHashMap(Function function) {
        return outFunctionHashMap.get(function);
    }

    public static void calculateInOut(Function function) {
        ArrayList<BasicBlock> basicBlocks = function.getBasicBlocks();
        boolean change = true;
        while (change) {
            change = false;
            for (int i = basicBlocks.size() - 1; i >= 0; i--) {
                BasicBlock basicBlock = basicBlocks.get(i);
                HashSet<Value> out = new HashSet<>();
                basicBlock.getBlockOutBasicBlock().forEach(
                        successor -> out.addAll(successor.getInBasicBlockHashSet()));
                addOutBlockHashSet(basicBlock, out);
                HashSet<Value> in = new HashSet<>(out);
                in.removeAll(basicBlock.getDefBasicBlockHashSet());
                in.addAll(basicBlock.getUseBasicBlockHashSet());
                HashSet<Value> originIn = basicBlock.getInBasicBlockHashSet();
                addInBlockHashSet(basicBlock, in);
                if (!in.equals(originIn)) {
                    change = true;
                }
            }
        }
    }

    public static HashSet<Value> getInBasicBlockHashSet(BasicBlock basicBlock) {
        return LivenessAnalysisController.getInFunctionHashMap(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static HashSet<Value> getDefBasicBlockHashSet(BasicBlock basicBlock) {
        return LivenessAnalysisController.getDefFunctionHashMap(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static HashMap<BasicBlock, HashSet<Value>> getDefFunctionHashMap(Function function) {
        return defFunctionHashMap.get(function);
    }

    public static HashSet<Value> getUseBasicBlockHashSet(BasicBlock basicBlock) {
        return LivenessAnalysisController.getUseFunctionHashMap(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static HashMap<BasicBlock, HashSet<Value>> getUseFunctionHashMap(Function function) {
        return useFunctionHashMap.get(function);
    }

    public static void addDefBlockHashSet(BasicBlock basicBlock, HashSet<Value> hashSet) {
        defFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        LivenessAnalysisController.getDefFunctionHashMap(basicBlock.getBelongFunction()).put(basicBlock, hashSet);
    }

    public static void addUseBlockHashSet(BasicBlock basicBlock, HashSet<Value> hashSet) {
        useFunctionHashMap.computeIfAbsent(basicBlock.getBelongFunction(), k -> new HashMap<>());
        LivenessAnalysisController.getUseFunctionHashMap(basicBlock.getBelongFunction()).put(basicBlock, hashSet);
    }
}

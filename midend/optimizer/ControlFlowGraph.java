package midend.optimizer;

import midend.generation.Values.Instruction.BasicInstruction.BrInstruction;
import midend.generation.Values.Instruction.BasicInstruction.JumpInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.Function;


import java.util.ArrayList;
import java.util.HashMap;


public class ControlFlowGraph {
    private static HashMap<Function, HashMap<BasicBlock, ArrayList<BasicBlock>>> indBasicBlockFunctionMap;
    private static HashMap<Function, HashMap<BasicBlock, ArrayList<BasicBlock>>> outBasicBlockFunctionMap;

    public static ArrayList<BasicBlock> getBlockIndBasicBlock(BasicBlock basicBlock) {
        ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction()).computeIfAbsent(basicBlock, k -> new ArrayList<>());
        return ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction()).get(basicBlock);
    }

    public static void build(LLvmIRModule module) {
        ControlFlowGraph.indBasicBlockFunctionMap = new HashMap<>();
        ControlFlowGraph.outBasicBlockFunctionMap = new HashMap<>();
        for (Function function : module.getFunctions()) {
            HashMap<BasicBlock, ArrayList<BasicBlock>> indBasicBlockMap = new HashMap<>();
            ControlFlowGraph.addFunctionIndBasicBlock(function, indBasicBlockMap);
            HashMap<BasicBlock, ArrayList<BasicBlock>> outBasicBlockMap = new HashMap<>();
            ControlFlowGraph.addFunctionOutBasicBlock(function, outBasicBlockMap);
            for (BasicBlock basicBlock : function.getBasicBlocks()) {
                ControlFlowGraph.addBlockIndBasicBlockList(basicBlock, new ArrayList<>());
                ControlFlowGraph.addBlockOutBasicBlockList(basicBlock, new ArrayList<>());
            }
            ControlFlowGraph.buildControlFlowGraph(function);
        }
    }

    private static void buildControlFlowGraph(Function function) {
        for (BasicBlock basicBlock : function.getBasicBlocks()) {
            Instruction lastInstr = basicBlock.getLastInstruction();
            if (lastInstr instanceof JumpInstruction jumpInstr) {
                ControlFlowGraph.addDoubleEdge(basicBlock, jumpInstr.getTarget());
            } else if (lastInstr instanceof BrInstruction brInstr) {
                ControlFlowGraph.addDoubleEdge(basicBlock, brInstr.getThenBlock());
                ControlFlowGraph.addDoubleEdge(basicBlock, brInstr.getElseBlock());
            }
        }
    }

    public static void addDoubleEdge(BasicBlock fromBlock, BasicBlock toBlock) {
        ControlFlowGraph.addBlockIndBasicBlock(toBlock, fromBlock);
        ControlFlowGraph.addBlockOutBasicBlock(fromBlock, toBlock);
    }

    public static void addBlockIndBasicBlock(BasicBlock basicBlock, BasicBlock indBasicBlock, int... idx) {
        ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction())
                .computeIfAbsent(basicBlock, k -> new ArrayList<>());
        if (idx.length == 1) {
            ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction())
                    .get(basicBlock).add(idx[0], indBasicBlock);
        } else {
            if (!ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction())
                    .get(basicBlock).contains(indBasicBlock)) {
                ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction())
                        .get(basicBlock).add(indBasicBlock);
            }
        }
    }

    public static void addBlockOutBasicBlock(BasicBlock basicBlock, BasicBlock outBasicBlock, int... idx) {
        ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction())
                .computeIfAbsent(basicBlock, k -> new ArrayList<>());
        if (idx.length == 1) {
            ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction())
                    .get(basicBlock).add(idx[0], outBasicBlock);
        } else {
            if (!ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction())
                    .get(basicBlock).contains(outBasicBlock)) {
                ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction())
                        .get(basicBlock).add(outBasicBlock);
            }
        }
    }

    public static HashMap<BasicBlock, ArrayList<BasicBlock>> getFunctionOutBasicBlock(Function function) {
        return ControlFlowGraph.outBasicBlockFunctionMap.get(function);
    }

    private static void addBlockOutBasicBlockList(BasicBlock basicBlock, ArrayList<BasicBlock> outBasicBlocks) {
        if (!ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction()).containsKey(basicBlock)) {
            ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction()).put(basicBlock, outBasicBlocks);
        }
    }

    private static void addBlockIndBasicBlockList(BasicBlock basicBlock, ArrayList<BasicBlock> indBasicBlocks) {
        if (!ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction()).containsKey(basicBlock)) {
            ControlFlowGraph.getFunctionIndBasicBlock(basicBlock.getBelongFunction()).put(basicBlock, indBasicBlocks);
        }
    }

    private static HashMap<BasicBlock, ArrayList<BasicBlock>> getFunctionIndBasicBlock(Function function) {
        return ControlFlowGraph.indBasicBlockFunctionMap.get(function);
    }

    private static void addFunctionOutBasicBlock(Function function, HashMap<BasicBlock, ArrayList<BasicBlock>> outBasicBlockMap) {
        ControlFlowGraph.outBasicBlockFunctionMap.put(function, outBasicBlockMap);
    }

    private static void addFunctionIndBasicBlock(Function function, HashMap<BasicBlock, ArrayList<BasicBlock>> indBasicBlockMap) {
        ControlFlowGraph.indBasicBlockFunctionMap.put(function, indBasicBlockMap);
    }


    public static ArrayList<BasicBlock> getBlockOutBasicBlock(BasicBlock basicBlock) {
        ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction()).computeIfAbsent(basicBlock, k -> new ArrayList<>());
        return ControlFlowGraph.getFunctionOutBasicBlock(basicBlock.getBelongFunction()).get(basicBlock);
    }
}

package midend.Units;

import midend.generation.Values.Instruction.BasicInstruction.BrInstruction;
import midend.generation.Values.Instruction.BasicInstruction.JumpInstruction;
import midend.generation.Values.Instruction.BasicInstruction.RetInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.UserPackage.Function;

import java.util.ArrayList;
import java.util.HashSet;

public class BlockOptimizer {
    public static void deleteDuplicateBranch(BasicBlock basicBlock) {
        for (int i = 0; i < basicBlock.getInstructionArrayList().size() - 1; i++) {
            Instruction instr = basicBlock.getInstructionArrayList().get(i);
            if (instr instanceof BrInstruction || instr instanceof JumpInstruction || instr instanceof RetInstruction) {
                basicBlock.setInstrArrayList(new ArrayList<>(basicBlock.getInstructionArrayList().subList(0, i + 1)));
                break;
            }
        }
    }

    public static void deleteDeadBlock(Function function) {
        HashSet<BasicBlock> vis = new HashSet<>();
        BasicBlock entry = function.getBasicBlocks().get(0);
        dfs(entry, vis);
        function.getBasicBlocks().removeIf(bb -> !vis.contains(bb) && bb.setDeleted(true));
    }

    private static void dfs(BasicBlock entry, HashSet<BasicBlock> vis) {
        vis.add(entry);
        Instruction lastInstr = entry.getLastInstruction();
        if (lastInstr instanceof BrInstruction branchInstr) {
            if (branchInstr.getThenBlock() != null && !vis.contains(branchInstr.getThenBlock())) {
                dfs(branchInstr.getThenBlock(), vis);
            }
            if (branchInstr.getElseBlock() != null && !vis.contains(branchInstr.getElseBlock())) {
                dfs(branchInstr.getElseBlock(), vis);
            }
        } else if (lastInstr instanceof JumpInstruction jumpInstr) {
            if (jumpInstr.getTarget() != null && !vis.contains(jumpInstr.getTarget())) {
                dfs(jumpInstr.getTarget(), vis);
            }
        }
    }
}

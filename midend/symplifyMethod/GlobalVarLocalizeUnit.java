package midend.symplifyMethod;

import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Values.Instruction.BasicInstruction.AllocaInstruction;
import midend.generation.Values.Instruction.BasicInstruction.StoreInstruction;
import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.Constant;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.User;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;
import midend.generation.Values.SubModule.BasicBlock;

import java.util.HashMap;
import java.util.HashSet;

public class GlobalVarLocalizeUnit {
    private static HashMap<GlobalVar, HashSet<Function>> globalVarUsers = new HashMap<>();

    public static void optimize(LLvmIRModule module) {
        module.getGlobalVars().forEach(globalVar -> globalVarUsers.put(globalVar, new HashSet<>())); //初始化
        module.getGlobalVars().forEach(GlobalVarLocalizeUnit::analysisGlobalVarUsers); //找到
        FunctionInlineUnit.buildFuncCallGraph();
        module.getGlobalVars().removeIf(GlobalVarLocalizeUnit::CanBelocalized);
    }

    /**
     * 如果全局变量没有使用者，那么就可以局部化
     * 如果全局变量只有一个使用者，那么就可以局部化
     */
    private static boolean CanBelocalized(GlobalVar globalVar) {
        if (globalVarUsers.get(globalVar).isEmpty()) {
            return true;
        }
        if (globalVarUsers.get(globalVar).size() == 1) {
            Function target = globalVarUsers.get(globalVar).iterator().next();
            if (!FunctionInlineUnit.getResponse(target).isEmpty()) {
                return false;
            }
            BasicBlock entry = target.getBasicBlocks().get(0);
            if (globalVar.getType().isInt32()) {
                AllocaInstruction allocaInstr = new AllocaInstruction(globalVar.getType(), LLvmIRNameGenerator.getLocalVarName(target));
                entry.insertInstruction(0, allocaInstr);
                for (Instruction instr : entry.getInstructionArrayList()) {
                    if (!(instr instanceof AllocaInstruction)) {
                        StoreInstruction storeInstr = new StoreInstruction(allocaInstr, new Constant(new VarType(32), String.valueOf(globalVar.getInitial().getInitValue())));
                        entry.insertInstruction(entry.getInstructionArrayList().indexOf(instr), storeInstr);
                        break;
                    }
                }
                globalVar.replaceUse(allocaInstr);
                return true;
            } else if (globalVar.getType().isInt8()) {
                AllocaInstruction allocaInstr = new AllocaInstruction(globalVar.getType(), LLvmIRNameGenerator.getLocalVarName(target));
                entry.insertInstruction(0, allocaInstr);
                for (Instruction instr : entry.getInstructionArrayList()) {
                    if (!(instr instanceof AllocaInstruction)) {
                        StoreInstruction storeInstr = new StoreInstruction(allocaInstr, new Constant(new VarType(8), String.valueOf(globalVar.getInitial().getInitValue())));
                        entry.insertInstruction(entry.getInstructionArrayList().indexOf(instr), storeInstr);
                        break;
                    }
                }
                globalVar.replaceUse(allocaInstr);
                return true;
            }
        }
        return false;
    }

    private static void analysisGlobalVarUsers(GlobalVar globalVar) {
        for (User user : globalVar.getUsers()) {
            if (user instanceof Instruction instruction) {
                globalVarUsers.get(globalVar).add(instruction.getBelongBasicBlock().getBelongFunction());
            }
        }
    }
}

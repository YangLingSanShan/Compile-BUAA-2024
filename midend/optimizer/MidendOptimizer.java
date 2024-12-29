package midend.optimizer;

import midend.generation.Values.SubModule.LLvmIRModule;
import midend.symplifyMethod.GlobalVarLocalizeUnit;
import midend.symplifyMethod.Mem2RegUnit;

public class MidendOptimizer extends Optimizer {

    private final LLvmIRModule module;

    private static boolean globalVarLocalizeSwitch;
    private static boolean mem2RegSwitch;
    private static boolean functionInlineSwitch;
    private static boolean globalVariableNumberingSwitch;
    private static boolean globalCodeMovementSwitch;
    private static boolean deadCodeEliminationSwitch;

    public MidendOptimizer(LLvmIRModule module) {
        this.module = module;
        this.init();
    }

    public static void build(LLvmIRModule module) {
        ControlFlowGraph.build(module);
        DominatorTree.build(module);
        LivenessAnalysisController.analysis(module);
    }

    private void init() {
        globalVarLocalizeSwitch = true;
        mem2RegSwitch = false;
        functionInlineSwitch = true;
        globalVariableNumberingSwitch = true;
        globalCodeMovementSwitch = true;
        deadCodeEliminationSwitch = true;
    }

    @Override
    public void optimize() {
        if (globalVarLocalizeSwitch) {
            GlobalVarLocalizeUnit.optimize(module);
        }
        if (mem2RegSwitch) {
            Mem2RegUnit.optimize(module);
        }
    }
}

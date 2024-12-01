package backend;

import midend.generation.Values.SubModule.LLvmIRModule;
import midend.optimizer.Optimizer;

public class BackendOptimizer extends Optimizer {

    private final LLvmIRModule module;
    private static boolean removePhiSwitch;
    private static boolean spaceOptimizerSwitch;
    private static boolean removeContinuousBranchSwitch;
    private static boolean removeDeadCodeSwitch;
    private static boolean basicBlockSortedSwitch;

    public BackendOptimizer(LLvmIRModule module) {
        this.module = module;
        this.init();
    }

    private void init() {
        removePhiSwitch = true;
        spaceOptimizerSwitch = true;
        removeContinuousBranchSwitch = true;
        removeDeadCodeSwitch = true;
        basicBlockSortedSwitch = true;
    }

    @Override
    public void optimize() {

    }
}

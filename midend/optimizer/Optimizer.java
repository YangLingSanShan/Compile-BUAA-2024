package midend.optimizer;

public abstract class Optimizer {

    private static boolean isOptimizer;

    public static boolean isOptimizer() {
        return isOptimizer;
    }

    public static void setOptimizer(boolean isOptimizerMode) {
        isOptimizer = isOptimizerMode;
    }

    public abstract void optimize();

}

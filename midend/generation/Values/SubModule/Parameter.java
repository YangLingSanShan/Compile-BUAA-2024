package midend.generation.Values.SubModule;

import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;
import midend.optimizer.Optimizer;

public class Parameter extends Value {
    private Function belongFunction;

    public Parameter(LLvmIRType type, String name) {
        super(type, name);
        //TODO: 优化
        if (!Optimizer.isOptimizer()) {
            LLvmIRNameGenerator.addParameter(this);
        }
    }

    public void setBelongFunction(Function belongFunction) {
        this.belongFunction = belongFunction;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}

package midend.generation.Values.SubModule;

import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Value;

public class Constant extends Value {

    private final int val;
    private final boolean isDefined;

    public Constant(LLvmIRType type, String num, boolean... isDefined) {
        super(type, num);
        this.val = Integer.parseInt(num);
        if (isDefined.length > 0) {
            this.isDefined = isDefined[0];
        } else {
            this.isDefined = true;
        }
    }

    public Integer getValue() {
        return val;
    }
}

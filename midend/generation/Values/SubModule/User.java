package midend.generation.Values.SubModule;

import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Value;

import java.util.ArrayList;

public class User extends Value {

    protected ArrayList<Value> operands;

    public User(LLvmIRType type, String name) {
        super(type, name);
        this.operands = new ArrayList<>();
    }

    public void addOperand(Value value, Integer... cnt) {
        int times = 1;
        if (cnt.length != 0) {
            times = cnt[0];
        }
        for (int i = 0; i < times; i++) {
            operands.add(value);
            if (value != null) {
                value.addUse(this);
            }
        }
    }

    public void replaceOperand(Value origin, Value present) {
        if (!operands.contains(origin)) {
            return;
        }
        operands.set(operands.indexOf(origin), present);
        replaceUse(origin, present, this);
    }

    public void dropOperands() {
        for (Value operand : operands) {
            operand.getUses().removeIf(use -> use.getUser().equals(this));
        }
    }

    public ArrayList<Value> getOperands() {
        return operands;
    }
}

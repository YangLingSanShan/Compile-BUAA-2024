package midend.generation.Values;

import midend.generation.Items.LLvmIRType;
import midend.generation.Values.Instruction.BasicInstruction.AllocaInstruction;
import midend.generation.Values.SubModule.User;
import midend.optimizer.Use;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Value {
    protected LLvmIRType type;

    protected String name;
    protected ArrayList<Use> uses;

    public Value(LLvmIRType type, String name) {
        this.type = type;
        this.name = name;
        this.uses = new ArrayList<>();
    }

    public void addUse(User user) {
        uses.add(new Use(user, this));
    }

    public LLvmIRType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void generateAssemble() {
    }

    public ArrayList<User> getUsers() {
        return uses.stream().map(Use::getUser).collect(Collectors.toCollection(ArrayList::new));
    }

    public void replaceUse(Value value) {
        ArrayList<Use> useDefCopy = new ArrayList<>(uses);
        for (Use use : useDefCopy) {
            User user = use.getUser();
            user.replaceOperand(this, value);
        }
    }

    public void replaceUse(Value origin, Value present, User user) {
        origin.deleteUse(user);
        present.addUse(user);
    }

    public void deleteUse(User user) {
        uses.removeIf(value -> value.getUser().equals(user));
    }
}

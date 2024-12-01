package midend.optimizer;

import midend.generation.Values.SubModule.User;
import midend.generation.Values.Value;

public class Use {
    private final User user;
    private final Value value;

    public Use(User user, Value value) {
        this.user = user;
        this.value = value;
    }

    public User getUser() {
        return user;
    }
}

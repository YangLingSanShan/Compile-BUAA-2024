package backend.AssembleCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import io.Settings;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.Value;

public class AssembleController {

    private static Integer currentOffset;
    private static AssembleRegisterController registerController = AssembleRegisterController.getInstance();
    private static HashMap<Value, Integer> stackOffsetHashMap;

    public static void init() {
        currentOffset = 0;
        registerController = AssembleRegisterController.getInstance();

    }

    public static void resetFunction(Function function) {
        currentOffset = 0;
        stackOffsetHashMap = new HashMap<>();
        registerController.setRegisterHashMap(function.getRegisterHashMap());
    }

    public static void moveCurrentOffset(int offset) {
        currentOffset += offset;
        if (currentOffset > 0) {
            Settings.getInstance().addErrorInfo("StackOverFlow");
        }
    }

    public static int getCurrentOffset() {
        return currentOffset;
    }

    public static void addOffset(Value value, int offset) {
        stackOffsetHashMap.put(value, offset);
    }

    public static Integer getOffset(Value value) {
        return stackOffsetHashMap.get(value);
    }

}

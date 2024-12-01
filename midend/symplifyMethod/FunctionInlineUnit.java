package midend.symplifyMethod;

import midend.generation.Generator;
import midend.generation.LLvmIR.LLvmIRGenerator;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FunctionInlineUnit {
    private LLvmIRModule module;
    private static boolean fixedPoint;
    private static HashMap<Function, ArrayList<Function>> callers;
    private static HashMap<Function, ArrayList<Function>> responses;
    private static ArrayList<Function> inlineFunctionsList;
    private static boolean isInlineAble;

    public static void buildFuncCallGraph() {
        FunctionInlineUnit.reset();
        Generator.getModule().getFunctions().forEach(Function::buildFuncCallGraph);
    }

    private static void reset() {
        FunctionInlineUnit.callers = new HashMap<>();
        FunctionInlineUnit.responses = new HashMap<>();
        for (Function function : Generator.getModule().getFunctions()) {
            FunctionInlineUnit.callers.put(function, new ArrayList<>());
            FunctionInlineUnit.responses.put(function, new ArrayList<>());
        }
    }

    public static void addCaller(Function caller, Function response) {
        FunctionInlineUnit.callers.get(caller).add(response);
    }

    public static ArrayList<Function> getCaller(Function caller) {
        return FunctionInlineUnit.callers.get(caller);
    }

    public static void addResponse(Function response, Function caller) {
        FunctionInlineUnit.responses.get(response).add(caller);
    }

    public static ArrayList<Function> getResponse(Function response) {
        return FunctionInlineUnit.responses.get(response);
    }
}

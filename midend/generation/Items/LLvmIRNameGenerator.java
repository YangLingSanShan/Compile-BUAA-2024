package midend.generation.Items;

import midend.generation.Values.Instruction.Instruction;
import midend.generation.Values.SubModule.BasicBlock;
import midend.generation.Values.SubModule.ControlFlow.Loop;
import midend.generation.Values.SubModule.FormatString;
import midend.generation.Values.SubModule.Parameter;
import midend.generation.Values.SubModule.UserPackage.Function;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;

import java.util.HashMap;
import java.util.Stack;


/**
 * IrNameController 是 LLVM IR 中的命名控制器，
 * 主要用于生成命名
 */
public class LLvmIRNameGenerator {
    /**
     * blockNameIndexHashMap 是该 IrNameController 的块名索引哈希表
     * paramNameIndex 是该 IrNameController 的参数名索引
     * stringLiteralNameIndex 是该 IrNameController 的字符串字面量名索引
     * localVarNameIndexHashMap 是该 IrNameController 的局部变量名索引哈希表
     * currentModule 是当前模块
     * currentBasicBlock 是当前基本块
     * currentFunction 是当前函数
     * loopProcedure 当前循环过程,
     */
    private static HashMap<Function, Integer> blockNameIndexes;
    private static HashMap<Function, Integer> localVarNameIndexes;
    private static Integer paramNameIndexes;
    private static Integer stringNameIndexes;
    private static LLvmIRModule nowLLvmIRModule;
    private static BasicBlock nowBasicBlock;
    private static Function nowFunction;
    private static Stack<Loop> loops;

    public static void init(LLvmIRModule module) {
        blockNameIndexes = new HashMap<>();
        paramNameIndexes = 0;
        stringNameIndexes = 0;
        localVarNameIndexes = new HashMap<>();

        nowLLvmIRModule = module;
        nowBasicBlock = null;
        nowFunction = null;
        loops = new Stack<>();
    }


    public static String getGlobalVarName(String symbolName) {
        return LLvmIRPerfix.GLOBAL_VAR_NAME + symbolName;
    }

    public static String getLocalVarName(Function... function) {
        Function presentFunction = ((function.length == 0) ? nowFunction : function[0]);
        int localVarNameIndex = localVarNameIndexes.get(presentFunction);
        localVarNameIndexes.put(presentFunction, localVarNameIndex + 1);
        return LLvmIRPerfix.LOCAL_VAR_NAME.toString() + localVarNameIndex;
    }

    public static String getFuncName(String funcName) {
        return (funcName.equals("main")) ? "@main" : LLvmIRPerfix.FUNC_NAME + funcName;
    }

    public static void setNowFunction(Function nowFunction) {
        LLvmIRNameGenerator.nowFunction = nowFunction;
        localVarNameIndexes.put(nowFunction, 0);
        blockNameIndexes.put(nowFunction, 0);
    }

    public static String getBlockName(Function... function) {
        // 使用当前函数或提供的第一个函数
        Function presentFunction = (function.length == 0) ? nowFunction : function[0];

        // 确保 blockNameIndexes 中存在当前函数的计数器
        blockNameIndexes.computeIfAbsent(presentFunction, k -> 0);

        // 获取当前计数器值并自增
        int blockNameIndex = blockNameIndexes.get(presentFunction);
        blockNameIndexes.put(presentFunction, blockNameIndex + 1);

        // 生成函数名称
        String funcName = presentFunction.getName().equals("@main") ? "main" :
                presentFunction.getName().substring(3);

        return String.format("%s_%s%d", funcName, LLvmIRPerfix.BB_NAME, blockNameIndex);
    }

    public static void setNowBasicBlock(BasicBlock basicBlock) {
        nowBasicBlock = basicBlock;
    }

    public static BasicBlock getNowBasicBlock() {
        return nowBasicBlock;
    }

    public static String getParameterName() {
        return LLvmIRPerfix.PARAM_NAME.toString() + paramNameIndexes++;
    }

    public static void pushLoop(Loop loop) {
        loops.push(loop);
    }

    public static void popLoop() {
        loops.pop();
    }

    public static Loop getNowLoop() {
        return loops.peek();
    }

    public static Function getNowFunc() {
        return nowFunction;
    }

    public static String getStringLiteralName() {
        return LLvmIRPerfix.STRING_LITERAL_NAME.toString() + stringNameIndexes++;
    }

    public static void addInstruction(Instruction instruction) {
        nowBasicBlock.addInstruction(instruction);
    }

    public static void addBasicBlock(BasicBlock basicBlock) {
        nowFunction.addBasicBlock(basicBlock);
    }

    public static void addFormatString(FormatString formatString) {
        nowLLvmIRModule.addString(formatString);
    }

    public static void addParameter(Parameter parameter) {
        nowFunction.addParameter(parameter);
    }

    public static void addFunction(Function function) {
        nowLLvmIRModule.addFunction(function);
    }

    public static void addGlobalVar(GlobalVar globalVar) {
        nowLLvmIRModule.addGlobalVar(globalVar);
    }

    public static void sub(Function... function) {
        Function presentFunction = ((function.length == 0) ? nowFunction : function[0]);
        int localVarNameIndex = localVarNameIndexes.get(presentFunction);
        localVarNameIndexes.put(presentFunction, localVarNameIndex - 1);
    }
}

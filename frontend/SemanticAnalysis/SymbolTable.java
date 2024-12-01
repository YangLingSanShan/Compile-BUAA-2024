package frontend.SemanticAnalysis;

import frontend.SemanticAnalysis.Symbols.FuncSymbol;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private static SymbolTable instance;

    private static HashMap<Integer, ArrayList<SymbolStack>> symbolStacks = new HashMap<>();

    private HashMap<Integer, SymbolStack> scopes = new HashMap<>();

    private int scopeNum = 1;

    private static FuncSymbol currentFuncSymbol;
    private static boolean isGlobal = true;

    private static int loopLevel = 0;

    private static int nowLevel = -1;


    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public static int getNowLevel() {
        return nowLevel;
    }

    public static void setNowFuncSymbol(FuncSymbol symbol) {
        currentFuncSymbol = symbol;
    }

    public void exitStack() {
        nowLevel--;
    }

    public static boolean isIsGlobal() {
        return isGlobal;
    }


    public void setGlobal(boolean flag) {
        this.isGlobal = flag;
    }

    public void pushStackTable() {
        nowLevel++;
        SymbolStack stack = new SymbolStack();
        symbolStacks.computeIfAbsent(nowLevel, k -> new ArrayList<>());
        symbolStacks.get(nowLevel).add(stack);
        scopes.put(scopeNum++, stack);    // 作用域
    }

    public void init() {
        symbolStacks = new HashMap<>();
        isGlobal = true;
        loopLevel = 0;
        nowLevel = -1;
        scopeNum = 1;
    }

    public boolean addSymbol(Symbol symbol) {
        // 获取当前的 SymbolStack
        ArrayList<SymbolStack> stacksAtLevel = symbolStacks.get(nowLevel);
        SymbolStack stack = stacksAtLevel.get(stacksAtLevel.size() - 1);
        // 如果符号已经存在，直接返回 false
        if (stack.getSymbol(symbol.getSymbolName()) != null) {
            return false;
        }
        // 否则添加符号并返回 true
        stack.addSymbol(symbol);
        return true;
    }

    public static Symbol getSymbol(String name) {
        int level = nowLevel;

        while (level >= 0) {
            ArrayList<SymbolStack> stacks = symbolStacks.get(level);  // 缓存 symbolStacks.get(level)
            if (stacks != null) {
                int size = stacks.size();  // 缓存 stacks.size()
                if (size > 0) {
                    SymbolStack topTable = stacks.get(size - 1);  // 获取栈顶符号表
                    Symbol symbol = topTable.getSymbol(name);
                    if (symbol != null) {
                        return symbol;
                    }
                }
            }
            level--;  // 继续查找上一级
        }

        return null;  // 如果未找到符号，则返回 null
    }

    public void enterForStmt() {
        loopLevel++;
        //pushStackTable();
    }

    public void exitForStmt() {
        loopLevel--;
        //exitStack();
    }

    public int getLoopLevel() {
        return loopLevel;
    }

    public FuncSymbol getCurrentFuncSymbol() {
        return currentFuncSymbol;
    }

    public HashMap<Integer, SymbolStack> getScopes() {
        return scopes;
    }

    public static void reset() {
        symbolStacks.clear();
        isGlobal = true;
        loopLevel = 0;
        nowLevel = -1;
    }
}

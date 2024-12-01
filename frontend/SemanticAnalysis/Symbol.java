package frontend.SemanticAnalysis;

import midend.generation.Values.SubModule.ControlFlow.Initial;
import midend.generation.Values.SubModule.UserPackage.GlobalVar;
import midend.generation.Values.Value;

import java.util.ArrayList;

public class Symbol {


    public enum type {
        CHAR, INT, VOID, CONST, VAR
    }

    private final String symbolName;
    private final type symbolType;
    private final Integer symbolLevel;

    public Symbol(String symbolName, type symbolType) {
        this.symbolName = symbolName;
        this.symbolType = symbolType;
        this.symbolLevel = SymbolTable.getNowLevel();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public type getSymbolType() {
        return symbolType;
    }

    public int getSymbolLevel() {
        return symbolLevel;
    }

    public Symbol.type getReturnType() {
        return symbolType;
    }

    public String getPrintType() {
        return "error";
    }

    public Initial getInitial() {
        return null;
    }

    public void setValue(Value value) {
    }

    public int getDim() {
        return -114514;
    }

    public ArrayList<Integer> getSpace() {
        return null;
    }

    public Value getValue() {
        return null;
    }

}

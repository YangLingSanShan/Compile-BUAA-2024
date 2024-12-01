package frontend.SemanticAnalysis.Symbols;

import frontend.SemanticAnalysis.Symbol;
import midend.generation.Values.SubModule.UserPackage.Function;

import java.util.ArrayList;
import java.util.Map;

public class FuncSymbol extends Symbol {

    private ArrayList<type> funcParamTypes;
    private ArrayList<Integer> funcParamDims;

    private Function function;

    public FuncSymbol(String symbolName, type symbolType) {
        super(symbolName, symbolType);
    }

    @Override
    public String getSymbolName() {
        return super.getSymbolName();
    }

    @Override
    public type getReturnType() {
        return super.getReturnType();
    }

    public void setParamInfo(ArrayList<type> funcParamTypes, ArrayList<Integer> funcParamDims) {
        this.funcParamTypes = funcParamTypes;
        this.funcParamDims = funcParamDims;
    }

    public int getParametersNum() {
        return funcParamTypes.size();
    }

    public ArrayList<type> getFuncParamTypes() {
        return funcParamTypes;
    }

    public ArrayList<Integer> getFuncParamDims() {
        return funcParamDims;
    }

    @Override
    public String getPrintType() {
        return switch (getReturnType()) {
            case VOID -> "VoidFunc";
            case INT -> "IntFunc";
            case CHAR -> "CharFunc";
            default -> "ERROR";
        };
    }

    public Function getfunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}

package frontend.SemanticAnalysis.Symbols;

import frontend.SemanticAnalysis.Symbol;
import io.Settings;
import midend.generation.Items.LLvmIRSpecificType.ArrayType;
import midend.generation.Items.LLvmIRSpecificType.VarType;
import midend.generation.Items.LLvmIRType;
import midend.generation.Values.SubModule.ControlFlow.Initial;
import midend.generation.Values.Value;

import java.util.ArrayList;

public class IntSymbol extends Symbol {

    private int dim;
    private ArrayList<Integer> initValue;
    private ArrayList<Integer> space;

    protected Value value;


    private Integer totalOffset;
    public IntSymbol(String symbolName, type symbolType, int dim, ArrayList<Integer> initValue, ArrayList<Integer> space) {
        super(symbolName, symbolType);
        this.dim = dim;
        this.initValue = initValue;
        this.space = space;
        this.totalOffset = (initValue == null) ? 0 : initValue.size();
        if (dim > 0) {
            int size = 1;
            for (int i = 0; i < dim; i++) {
                size *= space.get(i);
            }
            int res = size - totalOffset;
            for (int i = 0; i < res; i++) {
                this.initValue.add(0);
            }
        } else if (initValue.size() == 0 && dim == 0){
            this.initValue.add(0);
        }
    }

    @Override
    public String getPrintType() {
        return this.dim > 0 ? (this.getSymbolType() == type.CONST ? "ConstIntArray" : "IntArray") :
                (this.getSymbolType() == type.CONST ? "ConstInt" : "Int");
    }


    @Override
    public type getReturnType() {
        return type.INT;
    }

    @Override
    public int getDim() {
        return dim;
    }

    public Integer getConstValue(int... idx) {
        if (initValue == null || initValue.isEmpty()) {
            return 0;
        }
        if (idx.length == 0) {
            return initValue.get(0);
        } else if (idx.length == 1) {
            return initValue.get(idx[0]);
        } else {
            if (idx[0] == 0) {
                return initValue.get(idx[1]);
            }
            return initValue.get(idx[0] * space.get(0) + idx[1]);
        }
    }

    public void updateValue(int val, int... idx) {
        if (idx.length == 0) {
            initValue.set(0, val);
        } else if (idx.length == 1) {
            if (idx[0] >= initValue.size() || idx[0] < 0) {
                //TODO: 越界赋值
                Settings.getInstance().addErrorInfo("数组越界");
            } else {
                initValue.set(idx[0], val);
            }
        } else {
            if (idx[0] == 0) {
                initValue.set(idx[1], val);
            }
            initValue.set(idx[0] * space.get(0) + idx[1], val);
        }
    }

    @Override
    public Initial getInitial() {
        LLvmIRType type = (dim == 0) ? new VarType(32) : new ArrayType(space, new VarType(32));
        return new Initial(type, initValue, space, totalOffset);
    }

    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public ArrayList<Integer> getSpace() {
        return this.space;
    }

    @Override
    public Value getValue() {
        return value;
    }
}

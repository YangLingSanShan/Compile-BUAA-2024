package frontend.SemanticAnalysis;

import java.util.LinkedHashMap;

public class SymbolStack {
    private final LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();

    public void addSymbol(Symbol symbol) {
        symbols.put(symbol.getSymbolName(), symbol);
    }

    public Symbol getSymbol(String name) {
        return symbols.get(name);
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return symbols;
    }
}

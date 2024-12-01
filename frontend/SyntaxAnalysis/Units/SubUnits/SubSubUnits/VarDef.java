package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class VarDef extends Unit {
    public VarDef(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode vardefNode = new AstNode("<VarDef>");
        this.generateSubUnit(this.getRoot(), vardefNode);
        if (RecursionDown.getPreToken().getReserveWord().equals("ASSIGN")) {
            vardefNode.addChild(new AstNode("ASSIGN"));
            RecursionDown.nextToken();
            if (Checker.isInitVal()) {
                new InitVal(vardefNode);
            } else {
                throwParserError("InitVal");
            }
        }
    }
}
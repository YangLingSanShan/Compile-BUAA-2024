package frontend.SyntaxAnalysis.Units.SubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.FuncFParame;
import frontend.SyntaxAnalysis.Units.Unit;

public class FuncFParames extends Unit {
    public FuncFParames(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode funcFParamsNode = new AstNode("<FuncFParams>");
        this.getRoot().addChild(funcFParamsNode);
        while (Checker.isFuncFParam()) {
            new FuncFParame(funcFParamsNode);
            if (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
                funcFParamsNode.addChild(new AstNode("COMMA"));
                RecursionDown.nextToken();
            } else {
                break;
            }
        }
    }
}

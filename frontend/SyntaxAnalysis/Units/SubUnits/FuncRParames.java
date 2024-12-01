package frontend.SyntaxAnalysis.Units.SubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.Exp;
import frontend.SyntaxAnalysis.Units.Unit;

public class FuncRParames extends Unit {
    public FuncRParames(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode funcRParamsNode = new AstNode("<FuncRParams>");
        this.getRoot().addChild(funcRParamsNode);
        while (Checker.isExp()) {
            new Exp(funcRParamsNode);
            if (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
                funcRParamsNode.addChild(new AstNode("COMMA"));
                RecursionDown.nextToken();
            } else {
                break;
            }
        }
    }
}
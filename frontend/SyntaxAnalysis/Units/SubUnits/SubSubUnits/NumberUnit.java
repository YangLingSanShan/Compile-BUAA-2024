package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class NumberUnit extends Unit {
    public NumberUnit(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode numberNode = new AstNode("<Number>");
        this.getRoot().addChild(numberNode);
        numberNode.addChild(new AstNode("INTCON"));
        RecursionDown.nextToken();
    }
}
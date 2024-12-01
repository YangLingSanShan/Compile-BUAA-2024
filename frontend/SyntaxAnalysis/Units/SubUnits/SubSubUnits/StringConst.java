package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class StringConst extends Unit {
    public StringConst(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        this.getRoot().addChild(new AstNode("STRCON"));
        RecursionDown.nextToken();
    }
}

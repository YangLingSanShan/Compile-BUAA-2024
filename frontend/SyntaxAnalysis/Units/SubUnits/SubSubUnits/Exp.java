package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.Units.Unit;

public class Exp extends Unit {
    public Exp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode expNode = new AstNode("<Exp>");
        this.getRoot().addChild(expNode);
        new AddExp(expNode);
    }
}

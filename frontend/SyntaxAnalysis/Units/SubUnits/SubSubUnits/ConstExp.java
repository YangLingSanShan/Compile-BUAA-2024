package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.Units.Unit;

public class ConstExp extends Unit {
    public ConstExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode constExpNode = new AstNode("<ConstExp>");
        this.getRoot().addChild(constExpNode);
        new AddExp(constExpNode);
    }
}

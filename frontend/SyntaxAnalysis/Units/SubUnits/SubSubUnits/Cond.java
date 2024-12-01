package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Units.Unit;

public class Cond extends Unit {
    public Cond(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode condNode = new AstNode("<Cond>");
        this.getRoot().addChild(condNode);
        new LOrExp(condNode);
    }
}

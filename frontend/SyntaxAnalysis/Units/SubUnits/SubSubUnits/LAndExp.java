package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class LAndExp extends Unit {
    public LAndExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode landExpNode = new AstNode("<LAndExp>");
        new EqExp(landExpNode);
        while (RecursionDown.getPreToken().getReserveWord().equals("AND")) {
            AstNode extraLAndExpNode = new AstNode("<LAndExp>");
            extraLAndExpNode.addChild(landExpNode);
            landExpNode = extraLAndExpNode;
            landExpNode.addChild(new AstNode("AND"));
            RecursionDown.nextToken();
            new EqExp(landExpNode);
        }
        this.getRoot().addChild(landExpNode);
    }
}

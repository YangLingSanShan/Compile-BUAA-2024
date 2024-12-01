package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class LOrExp extends Unit {
    public LOrExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode lorExpNode = new AstNode("<LOrExp>");
        new LAndExp(lorExpNode);
        while (RecursionDown.getPreToken().getReserveWord().equals("OR")) {
            AstNode extraLorExpNode = new AstNode("<LOrExp>");
            extraLorExpNode.addChild(lorExpNode);
            lorExpNode = extraLorExpNode;
            lorExpNode.addChild(new AstNode("OR"));
            RecursionDown.nextToken();
            new LAndExp(lorExpNode);
        }
        this.getRoot().addChild(lorExpNode);
    }
}

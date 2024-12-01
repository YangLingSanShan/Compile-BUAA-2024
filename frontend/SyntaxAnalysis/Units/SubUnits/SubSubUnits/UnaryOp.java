package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class UnaryOp extends Unit {
    public UnaryOp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode unaryOpNode = new AstNode("<UnaryOp>");
        this.getRoot().addChild(unaryOpNode);
        unaryOpNode.addChild(new AstNode(RecursionDown.getPreToken().getReserveWord()));
        RecursionDown.nextToken();
    }
}
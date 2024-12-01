package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class MulExp extends Unit {
    public MulExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode mulExpNode = new AstNode("<MulExp>");
        new UnaryExp(mulExpNode);
        String operator;
        while (isMulExpOperator(operator = RecursionDown.getPreToken().getReserveWord())) {
            AstNode newMulExpNode = new AstNode("<MulExp>");
            newMulExpNode.addChild(mulExpNode);
            mulExpNode = newMulExpNode;

            mulExpNode.addChild(new AstNode(operator));
            RecursionDown.nextToken();
            new UnaryExp(mulExpNode);
        }

        this.getRoot().addChild(mulExpNode);
    }

    private boolean isMulExpOperator(String token) {
        return token.equals("MULT") || token.equals("DIV") || token.equals("MOD");
    }
}

package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class EqExp extends Unit {
    public EqExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode eqExpNode = new AstNode("<EqExp>");
        new RelExp(eqExpNode);
        String operator;
        while (isEqOperator(operator = RecursionDown.getPreToken().getReserveWord())) {
            AstNode extraEqExpNode = new AstNode("<EqExp>");
            extraEqExpNode.addChild(eqExpNode);
            eqExpNode = extraEqExpNode;
            eqExpNode.addChild(new AstNode(operator));
            RecursionDown.nextToken();
            new RelExp(eqExpNode);
        }
        this.getRoot().addChild(eqExpNode);
    }

    private boolean isEqOperator(String token) {
        return token.equals("EQL") || token.equals("NEQ");
    }

}

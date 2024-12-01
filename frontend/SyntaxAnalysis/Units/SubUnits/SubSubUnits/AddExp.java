package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class AddExp extends Unit {
    public AddExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode addExpNode = new AstNode("<AddExp>");
        new MulExp(addExpNode);
        String operator;
        while (isAddOperator(operator = RecursionDown.getPreToken().getReserveWord())) {
            AstNode newAddExpNode = new AstNode("<AddExp>");
            newAddExpNode.addChild(addExpNode);
            addExpNode = newAddExpNode;
            addExpNode.addChild(new AstNode(operator));
            RecursionDown.nextToken();
            new MulExp(addExpNode);
        }
        // AddExp → MulExp {('+' | '−') MulExp}
        this.getRoot().addChild(addExpNode);
    }

    private boolean isAddOperator(String token) {
        return token.equals("PLUS") || token.equals("MINU");
    }

}

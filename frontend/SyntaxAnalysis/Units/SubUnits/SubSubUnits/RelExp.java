package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class RelExp extends Unit {
    public RelExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode relExpNode = new AstNode("<RelExp>");
        new AddExp(relExpNode);
        String operator;
        while (isRelOperator(operator = RecursionDown.getPreToken().getReserveWord())) {
            AstNode extraRelExpNode = new AstNode("<RelExp>");
            extraRelExpNode.addChild(relExpNode);
            relExpNode = extraRelExpNode;
            relExpNode.addChild(new AstNode(operator));
            RecursionDown.nextToken();
            new AddExp(relExpNode);
        }
        this.getRoot().addChild(relExpNode);
    }

    private boolean isRelOperator(String token) {
        return token.equals("LSS") || token.equals("LEQ") ||
                token.equals("GRE") || token.equals("GEQ");
    }

}

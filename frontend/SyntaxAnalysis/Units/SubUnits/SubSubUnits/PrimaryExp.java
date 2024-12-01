package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class PrimaryExp extends Unit {
    public PrimaryExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode primaryExpNode = new AstNode("<PrimaryExp>");
        this.getRoot().addChild(primaryExpNode);

        String preToken = RecursionDown.getPreToken().getReserveWord();
        if (preToken.equals("LPARENT")) {
            handleParentheses(primaryExpNode);
        } else if (Checker.isIdent()) {
            new LVal(primaryExpNode);
        } else if (Checker.isNumber()) {
            new NumberUnit(primaryExpNode);
        } else if (Checker.isCharacter()) {
            new CharacterUnit(primaryExpNode);
        } else {
            this.throwParserError("PrimaryExp");
        }
    }

    private void handleParentheses(AstNode primaryExpNode) {
        primaryExpNode.addChild(new AstNode("LPARENT"));
        RecursionDown.nextToken();

        if (!Checker.isExp()) {
            this.throwParserError("Exp");
        } else {
            new Exp(primaryExpNode);
        }

        this.syntaxCheck('j', "RPARENT", primaryExpNode);
    }

    // PrimaryExp → '(' Exp ')' | LVal | Number | Character// j
}
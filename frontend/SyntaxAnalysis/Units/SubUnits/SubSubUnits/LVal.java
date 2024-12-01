package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class LVal extends Unit {
    public LVal(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode lValNode = new AstNode("<LVal>");
        this.getRoot().addChild(lValNode);
        lValNode.addChild(new AstNode("IDENFR"));
        RecursionDown.nextToken();
        while (RecursionDown.getPreToken().getReserveWord().equals("LBRACK")) {
            lValNode.addChild(new AstNode("LBRACK"));
            RecursionDown.nextToken();
            if (Checker.isExp()) {
                new Exp(lValNode);
            } else {
                this.throwParserError("Exp");
            }
            this.syntaxCheck('k', "RBRACK", lValNode);
        }

    }
    //LVal → Ident ['[' Exp ']'] // c k
}

package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class Printf extends Unit {
    public Printf(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode printfStmtNode = new AstNode("PRINTFTK");
        this.getRoot().addChild(printfStmtNode);
        RecursionDown.nextToken();

        if (!RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            this.throwParserError("LPARENT");
        }

        this.getRoot().addChild(new AstNode("LPARENT"));
        RecursionDown.nextToken();

        if (!RecursionDown.getPreToken().getReserveWord().equals("STRCON")) {
            this.throwParserError("STRCON");
        }

        this.getRoot().addChild(new AstNode("STRCON"));
        RecursionDown.nextToken();

        while (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
            this.getRoot().addChild(new AstNode("COMMA"));
            RecursionDown.nextToken();
            if (!Checker.isExp()) {
                this.throwParserError("Exp");
            } else {
                new Exp(this.getRoot());
            }
        }

        this.syntaxCheck('j', "RPARENT", this.getRoot());
        this.syntaxCheck('i', "SEMICN", this.getRoot());
    }

}

package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

import java.io.IOException;

public class InitVal extends Unit {
    public InitVal(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode initValNode = new AstNode("<InitVal>");
        this.getRoot().addChild(initValNode);

        if (Checker.isExp()) {
            new Exp(initValNode);
        } else if (Checker.isStringConst()) {
            new StringConst(initValNode);
        } else if (RecursionDown.getPreToken().getReserveWord().equals("LBRACE")) {
            handleBraceInitialization(initValNode);
        } else {
            this.throwParserError("InitVal");
        }
    }

    private void handleBraceInitialization(AstNode initValNode) {
        initValNode.addChild(new AstNode("LBRACE"));
        RecursionDown.nextToken();

        if (Checker.isExp()) {
            new Exp(initValNode);
        }

        while (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
            initValNode.addChild(new AstNode("COMMA"));
            RecursionDown.nextToken();

            if (Checker.isExp()) {
                new Exp(initValNode);
            } else {
                this.throwParserError("InitVal");
            }
        }

        if (!RecursionDown.getPreToken().getReserveWord().equals("RBRACE")) {
            this.throwParserError("RBRACE");
        } else {
            initValNode.addChild(new AstNode("RBRACE"));
            RecursionDown.nextToken();
        }
    }

}


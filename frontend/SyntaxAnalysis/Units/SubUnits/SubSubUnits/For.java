package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class For extends Unit {
    public For(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode forStmtNode = new AstNode("FORTK");
        this.getRoot().addChild(forStmtNode);
        RecursionDown.nextToken();

        if (!RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            this.throwParserError("LPARENT");
        }

        this.getRoot().addChild(new AstNode("LPARENT"));
        RecursionDown.nextToken();

        if (Checker.isForStmtVal()) {
            new ForStmt(this.getRoot());
        } else {
            this.throwParserError("ForStmtVal");
        }

        this.syntaxCheck('i', "SEMICN", this.getRoot());

        if (Checker.isCond()) {
            new Cond(this.getRoot());
        } else {
            this.throwParserError("Cond");
        }

        this.syntaxCheck('i', "SEMICN", this.getRoot());

        if (Checker.isForStmtVal()) {
            new ForStmt(this.getRoot());
        } else {
            this.throwParserError("ForStmtVal");
        }

        this.syntaxCheck('j', "RPARENT", this.getRoot());

        if (Checker.isStmt()) {
            new Stmt(this.getRoot());
        } else {
            this.throwParserError("Stmt");
        }
    }

}

package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class If extends Unit {
    public If(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        this.getRoot().addChild(new AstNode("IFTK"));
        RecursionDown.nextToken();

        if (!RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            this.throwParserError("IF");// 如果没有 LPARENT，提前返回
        }

        this.getRoot().addChild(new AstNode("LPARENT"));
        RecursionDown.nextToken();

        if (!Checker.isCond()) {
            this.throwParserError("Cond");// 如果不是条件语句
        }
        new Cond(this.getRoot());

        this.syntaxCheck('j', "RPARENT", this.getRoot());

        if (!Checker.isStmt()) {
            this.throwParserError("Stmt");// 如果语句不合法，提前返回
        }
        new Stmt(this.getRoot());

        if (RecursionDown.getPreToken().getReserveWord().equals("ELSETK")) {
            this.getRoot().addChild(new AstNode("ELSETK"));
            RecursionDown.nextToken();

            if (!Checker.isStmt()) {
                this.throwParserError("Stmt");
            } else {
                new Stmt(this.getRoot());
            }
        }
    }

}

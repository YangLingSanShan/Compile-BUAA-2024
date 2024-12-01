package frontend.SyntaxAnalysis.Units.SubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.ConstDef;
import frontend.SyntaxAnalysis.Units.Unit;

public class ConstDecl extends Unit {
    public ConstDecl(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode constDeclNode = new AstNode("<ConstDecl>");
        this.getRoot().addChild(constDeclNode);
        constDeclNode.addChild(new AstNode("CONSTTK"));
        RecursionDown.nextToken();

        if (!Checker.isBType()) {
            this.throwParserError("ConstDecl");
        }

        AstNode BNode = new AstNode("<BType>");
        constDeclNode.addChild(BNode);
        BNode.addChild(new AstNode(RecursionDown.getPreToken().getReserveWord()));
        RecursionDown.nextToken();

        while (true) {
            if (Checker.isConstDef()) {
                new ConstDef(constDeclNode);
            } else {
                this.throwParserError("ConstDef");
            }

            if (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
                constDeclNode.addChild(new AstNode("COMMA"));
                RecursionDown.nextToken();
            } else {
                break; // 没有逗号时退出循环
            }
        }

        this.syntaxCheck('i', "SEMICN", constDeclNode);
    }

}

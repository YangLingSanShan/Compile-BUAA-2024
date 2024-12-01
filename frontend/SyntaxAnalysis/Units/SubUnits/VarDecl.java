package frontend.SyntaxAnalysis.Units.SubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.VarDef;
import frontend.SyntaxAnalysis.Units.Unit;

public class VarDecl extends Unit {
    public VarDecl(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode varDeclNode = new AstNode("<VarDecl>");
        this.getRoot().addChild(varDeclNode);

        if (!Checker.isBType()) {
            this.throwParserError("BType");
        }

        AstNode BNode = new AstNode("<BType>");
        varDeclNode.addChild(BNode);
        BNode.addChild(new AstNode(RecursionDown.getPreToken().getReserveWord()));
        RecursionDown.nextToken();

        while (true) {
            if (Checker.isVarDef()) {
                new VarDef(varDeclNode);
            } else {
                this.throwParserError("VarDef");
            }

            if (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
                varDeclNode.addChild(new AstNode("COMMA"));
                RecursionDown.nextToken();
            } else {
                break; // 没有逗号时退出循环
            }
        }

        this.syntaxCheck('i', "SEMICN", varDeclNode);
    }

}
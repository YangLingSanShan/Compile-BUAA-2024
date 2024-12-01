package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class ConstInitVal extends Unit {

    public ConstInitVal(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        //ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' | StringConst
        AstNode constInitValNode = new AstNode("<ConstInitVal>");
        this.getRoot().addChild(constInitValNode);
        if (Checker.isConstExp()) {
            new ConstExp(constInitValNode);
        } else if (Checker.isStringConst()) {
            new StringConst(constInitValNode);
        } else if (RecursionDown.getPreToken().getReserveWord().equals("LBRACE")) {
            constInitValNode.addChild(new AstNode("LBRACE"));
            RecursionDown.nextToken();
            if (Checker.isConstExp()) {
                new ConstExp(constInitValNode);
            }
            while (RecursionDown.getPreToken().getReserveWord().equals("COMMA")) {
                constInitValNode.addChild(new AstNode("COMMA"));
                RecursionDown.nextToken();
                if (Checker.isConstExp()) {
                    new ConstExp(constInitValNode);
                } else {
                    this.throwParserError("ConstExp");
                }
            }
            if (RecursionDown.getPreToken().getReserveWord().equals("RBRACE")) {
                constInitValNode.addChild(new AstNode("RBRACE"));
                RecursionDown.nextToken();
            } else {
                this.throwParserError("RBRACE");
            }
        } else {
            this.throwParserError("ConstExp");
        }
    }
}

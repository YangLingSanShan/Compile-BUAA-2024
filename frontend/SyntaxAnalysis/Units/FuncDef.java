package frontend.SyntaxAnalysis.Units;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.FuncType;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.Block;
import frontend.SyntaxAnalysis.Units.SubUnits.FuncFParames;

public class FuncDef extends Unit {

    public FuncDef(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode funcDefNode = new AstNode("<FuncDef>");
        this.getRoot().addChild(funcDefNode);
        if (Checker.isFuncType()) {
            new FuncType(funcDefNode);
        } else {
            this.throwParserError("FuncType");
        }
        if (Checker.isIdent()) {
            funcDefNode.addChild(new AstNode("IDENFER"));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("IDENFER");
        }
        if (RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            funcDefNode.addChild(new AstNode("LPARENT"));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("LPARENT");
        }
        if (Checker.isFuncFParams()) {
            new FuncFParames(funcDefNode);
        }
        this.syntaxCheck('j', "RPARENT", funcDefNode);
        if (Checker.isBlock()) {
            new Block(funcDefNode);
        } else {
            this.throwParserError("BLOCK");
        }
    }
}
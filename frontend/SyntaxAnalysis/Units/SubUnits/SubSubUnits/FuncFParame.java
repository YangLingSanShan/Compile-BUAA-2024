package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class FuncFParame extends Unit {

    public FuncFParame(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode funcFParamNode = new AstNode("<FuncFParam>");
        this.getRoot().addChild(funcFParamNode);
        if (RecursionDown.getPreToken().getReserveWord().equals("INTTK") ||
                RecursionDown.getPreToken().getReserveWord().equals("CHARTK")) {
            AstNode btypeNode = new AstNode("<BType>");
            funcFParamNode.addChild(btypeNode);
            btypeNode.addChild(new AstNode(RecursionDown.getPreToken().getReserveWord()));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("BType");
        }
        if (Checker.isIdent()) {
            funcFParamNode.addChild(new AstNode("IDENFR"));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("IDENFR");
        }
        if (RecursionDown.getPreToken().getReserveWord().equals("LBRACK")) {
            AstNode lbrackNode = new AstNode("LBRACK");
            funcFParamNode.addChild(lbrackNode);
            RecursionDown.nextToken();
            this.syntaxCheck('k', "RBRACK", funcFParamNode);
        }
        while (RecursionDown.getPreToken().getReserveWord().equals("LBRACK")) {
            AstNode lbrackNode = new AstNode("LBRACK");
            funcFParamNode.addChild(lbrackNode);
            RecursionDown.nextToken();
            this.syntaxCheck('k', "RBRACK", funcFParamNode);
        }
    }
}

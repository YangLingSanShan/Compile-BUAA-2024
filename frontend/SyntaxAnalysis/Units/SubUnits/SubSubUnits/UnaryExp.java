package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.FuncRParames;
import frontend.SyntaxAnalysis.Units.Unit;

public class UnaryExp extends Unit {
    public UnaryExp(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode unaryExpNode = new AstNode("<UnaryExp>");
        this.getRoot().addChild(unaryExpNode);

        if (Checker.isUnaryOp()) {
            new UnaryOp(unaryExpNode);
            if (!Checker.isUnaryExp()) {
                this.throwParserError("UnaryExp");
            } else {
                new UnaryExp(unaryExpNode);
            }
        } else if (Checker.isIdent() && RecursionDown.getPositionToken(1).getReserveWord().equals("LPARENT")) {
            handleFunctionCall(unaryExpNode);
        } else if (Checker.isPrimaryExp()) {
            new PrimaryExp(unaryExpNode);
        } else {
            this.throwParserError("UnaryExp");
        }
    }

    private void handleFunctionCall(AstNode unaryExpNode) {
        unaryExpNode.addChild(new AstNode("IDENFR"));
        RecursionDown.nextToken();

        if (RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            unaryExpNode.addChild(new AstNode("LPARENT"));
            RecursionDown.nextToken();

            if (Checker.isFuncRParams()) {
                new FuncRParames(unaryExpNode);
            }
            this.syntaxCheck('j', "RPARENT", unaryExpNode);
        }
    }

    //PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp // c d e j
}
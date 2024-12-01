package frontend.SyntaxAnalysis.Units.SubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class FuncType extends Unit {
    public FuncType(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode funcTypeNode = new AstNode("<FuncType>");
        this.getRoot().addChild(funcTypeNode);

        String preToken = RecursionDown.getPreToken().getReserveWord();
        if (isValidFuncType(preToken)) {
            funcTypeNode.addChild(new AstNode(preToken));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("FuncType");
        }
    }

    private boolean isValidFuncType(String token) {
        return token.equals("INTTK") || token.equals("CHARTK") || token.equals("VOIDTK");
    }

}

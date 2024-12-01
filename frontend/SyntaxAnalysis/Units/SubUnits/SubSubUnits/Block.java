package frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.Unit;

public class Block extends Unit {
    public Block(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode blockNode = new AstNode("<Block>");
        this.getRoot().addChild(blockNode);
        blockNode.addChild(new AstNode("LBRACE"));
        RecursionDown.nextToken();
        while (Checker.isBlockItem()) {
            new BlockItem(blockNode);
        }
        if (RecursionDown.getPreToken().getReserveWord().equals("RBRACE")) {
            blockNode.addChild(new AstNode("RBRACE"));
            RecursionDown.nextToken();
        } else {
            this.throwParserError("RBRACE");
        }
    }
}

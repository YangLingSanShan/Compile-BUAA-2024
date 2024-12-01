package frontend.SyntaxAnalysis.Units;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.Checker;
import frontend.SyntaxAnalysis.RecursionDown;
import frontend.SyntaxAnalysis.Units.SubUnits.SubSubUnits.Block;

public class MainFuncDef extends Unit {

    public MainFuncDef(AstNode root) {
        super(root);
        this.analysis();
    }

    @Override
    public void analysis() {
        AstNode mainFuncDefNode = new AstNode("<MainFuncDef>");
        this.getRoot().addChild(mainFuncDefNode);

        // 检查是否为 'INTTK' 关键字
        if (!RecursionDown.getPreToken().getReserveWord().equals("INTTK")) {
            this.throwParserError("INTTK");
        }
        mainFuncDefNode.addChild(new AstNode("INTTK"));
        RecursionDown.nextToken();

        // 检查是否为 'MAINTK' 关键字
        if (!RecursionDown.getPreToken().getReserveWord().equals("MAINTK")) {
            this.throwParserError("MAINTK");
        }
        mainFuncDefNode.addChild(new AstNode("MAINTK"));
        RecursionDown.nextToken();

        // 检查是否为左括号 'LPARENT'
        if (!RecursionDown.getPreToken().getReserveWord().equals("LPARENT")) {
            this.throwParserError("LPARENT");
        }
        mainFuncDefNode.addChild(new AstNode("LPARENT"));
        RecursionDown.nextToken();

        // 检查是否为右括号 'RPARENT'
        this.syntaxCheck('j', "RPARENT", mainFuncDefNode);

        // 检查是否为函数体块 'Block'
        if (!Checker.isBlock()) {
            this.throwParserError("Block");
        }
        new Block(mainFuncDefNode);
    }

}

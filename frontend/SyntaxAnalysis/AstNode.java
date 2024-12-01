package frontend.SyntaxAnalysis;

import frontend.LexerAnalysis.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AstNode {
    private String grammarType;

    private ArrayList<AstNode> children = new ArrayList<>();

    private AstNode parent;

    private Token nowToken;

    private HashMap<String, Integer> Scope = new HashMap<>(Map.of("start", -1, "end", -1));

    public AstNode(String grammarType) {
        this.grammarType = grammarType;
        this.nowToken = RecursionDown.getPreToken();
        if (this.grammarType.matches("<.*>")) {     //not end
            this.changeStart(this.nowToken.getLineNumber());
        } else {
            this.changeStart(this.nowToken.getLineNumber());
            this.changeEnd(this.nowToken.getLineNumber());
            RecursionDown.setPreAst(this);      //上一个语法树节点设置好
        }
    }

    public void changeStart(int startLine) {
        this.Scope.replace("start", startLine);
    }

    public void changeEnd(int endLine) {
        this.Scope.replace("end", endLine);
    }

    public int getStart() {
        return Scope.get("start");
    }

    public int getEnd() {
        return Scope.get("end");
    }

    public void addChild(AstNode child) {
        this.children.add(child);
        child.setParent(this);
        if (child.getEnd() != -1) {
            AstNode t = this;
            while (t != null && t.getEnd() < child.getEnd()) {
                t.setEnd(child.getEnd());
                t = t.parent;
            }
        }
    }

    private void setEnd(int end) {
        this.Scope.replace("end", end);
    }

    public void setParent(AstNode parent) {
        this.parent = parent;
    }

    public AstNode getParent() {
        return this.parent;
    }

    public ArrayList<AstNode> getChildren() {
        return this.children;
    }

    public String getGrammar() {
        return this.grammarType;
    }

    public void deleteChild(AstNode expNode) {
        this.children.remove(expNode);
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public String toString() {
        return nowToken.getReserveWord() + " " + nowToken.getOriginWord();
    }

    public Token getToken() {
        return nowToken;
    }
}

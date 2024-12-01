package frontend;

import frontend.SyntaxAnalysis.AstNode;
import frontend.SyntaxAnalysis.RecursionDown;
import io.Settings;

public class SyntaxParser {

    private static AstNode root;

    public static void analyze() {
        RecursionDown recursionDown = new RecursionDown();
        SyntaxParser.root = new AstNode("<CompUnit>");
        //TODO: 执行递归下降
        recursionDown.beginRecursionDown(root);
        retrace(root);
    }

    private static void retrace(AstNode rootNode) {
        for (AstNode astNode : rootNode.getChildren()) {
            retrace(astNode);
        }
        String line = "";
        if (rootNode.isLeaf()) {
            line = rootNode.toString();
            Settings.getInstance().addSyntaxLine(line);
        } else if (!(rootNode.getGrammar().equals("<BlockItem>")
                || rootNode.getGrammar().equals("<BType>")
                || rootNode.getGrammar().equals("<Decl>"))) {
            line = rootNode.getGrammar();
            Settings.getInstance().addSyntaxLine(line);
        }

    }

    public static AstNode getRoot() {
        return root;
    }
}

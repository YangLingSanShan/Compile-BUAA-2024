package midend.generation;

import frontend.SemanticAnalysis.SymbolTable;
import frontend.SyntaxAnalysis.AstNode;

import io.Settings;
import midend.generation.Items.LLvmIRNameGenerator;
import midend.generation.Values.SubModule.LLvmIRModule;
import midend.generation.LLvmIR.LLvmIRGenerator;
import midend.optimizer.Optimizer;

public class Generator {

    private static LLvmIRGenerator lLvmIRGenerator;       //生成 LLVM IR 的核心解释器
    private static LLvmIRModule module;             //LLVM IR的顶级模块

    public static void generate(AstNode root) {
        module = new LLvmIRModule();
        SymbolTable.reset();
        LLvmIRNameGenerator.init(module);

        Optimizer.setOptimizer(false);
        lLvmIRGenerator = new LLvmIRGenerator();
        lLvmIRGenerator.llvmIRAnalyse(root);


        String llvm = module.toString();
        Settings.getInstance().addLLvmIROutput(llvm);
        Settings.getInstance().printLLvmIr(llvm, "testfilei22371237李佩儒_优化前中间代码.txt");

        Optimizer.setOptimizer(true);
    }

    public static void preTraverse(AstNode rootAst) {
        if (rootAst.isLeaf()) {
            return;
        }
        for (AstNode astNode : rootAst.getChildren()) {
            lLvmIRGenerator.llvmIRAnalyse(astNode);
            if (astNode.getGrammar().matches("IFTK|FORTK|BREAKTK|CONTINUETK|PRINTFTK|ASSIGN|RETURNTK")) {
                break;
            }
        }
    }

    public static LLvmIRModule getModule() {
        return module;
    }
}

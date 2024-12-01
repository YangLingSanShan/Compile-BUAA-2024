import java.io.IOException;
import java.nio.charset.StandardCharsets;

import backend.BackendOptimizer;
import frontend.Lexer;
import frontend.SemanticParser;
import frontend.SyntaxParser;
import io.Settings;
import midend.optimizer.MidendOptimizer;
import midend.generation.Generator;
import backend.AssembleGenerator;

public class Compiler {
    public static void main(String[] args) throws IOException {
        Settings settings = Settings.getInstance();
        String line;
        Lexer lexer = new Lexer();

        for (int lineNum = 1; (line = settings.getFileInputBuffer().readLine()) != null; lineNum++) {
            lexer.analyze(line, lineNum);
        }

        SyntaxParser.analyze();
        SemanticParser.analysis(SyntaxParser.getRoot());

        if (settings.isGenerationMode() && !settings.isHaveError()) {
            Generator.generate(SyntaxParser.getRoot());
            if (settings.isOptimize()) {
                if (true) {     //中端开关
                    MidendOptimizer midendOptimizer = new MidendOptimizer(Generator.getModule());
                    midendOptimizer.optimize();
                }
                settings.printLLvmIr(Generator.getModule().toString(), "testfilei22371237李佩儒_优化后中间代码.txt");
                if (true) {     //后端开关
                    BackendOptimizer backendOptimizer = new BackendOptimizer(Generator.getModule());
                    backendOptimizer.optimize();
                }
            }
            AssembleGenerator.generate(Generator.getModule());
        }
        settings.print();
    }
}

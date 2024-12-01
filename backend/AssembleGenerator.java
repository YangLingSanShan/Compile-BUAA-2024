package backend;

import backend.AssembleCodes.AssembleController;
import backend.AssembleCodes.AssembleCodes;
import io.Settings;
import midend.generation.Values.SubModule.LLvmIRModule;

public class AssembleGenerator {

    private static AssembleCodes assembleCodes;

    public static void generate(LLvmIRModule module) {
        assembleCodes = AssembleCodes.getInstance();
        AssembleController.init();
        module.generateAssemble();
        //输出
        Settings.getInstance().addAssembleOutput(assembleCodes.toString());
    }
}

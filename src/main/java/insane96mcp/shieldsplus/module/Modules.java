package insane96mcp.shieldsplus.module;


import insane96mcp.insanelib.base.Module;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.Config;
import net.minecraftforge.fml.config.ModConfig;

public class Modules {
    public static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(ShieldsPlus.MOD_ID, "base", "Shields Plus", ModConfig.Type.COMMON, Config.builder)
                .canBeDisabled(false)
                .build();
    }
}

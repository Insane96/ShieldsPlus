package com.insane96mcp.shieldsplus.module;


import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.setup.Config;
import insane96mcp.insanelib.base.Module;
import net.minecraftforge.fml.config.ModConfig;

public class Modules {
    public static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(ShieldsPlus.RESOURCE_PREFIX + "base", "Shields Plus", ModConfig.Type.COMMON, Config.builder)
                .canBeDisabled(false)
                .build();
    }
}

package com.insane96mcp.shieldsplus.module;


import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.setup.Config;
import insane96mcp.insanelib.base.Module;

public class Modules {
    public static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(Config.builder, ShieldsPlus.RESOURCE_PREFIX + "base", "Shields Plus")
                .canBeDisabled(false)
                .build();
    }
}

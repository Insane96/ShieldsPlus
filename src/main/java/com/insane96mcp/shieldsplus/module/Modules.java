package com.insane96mcp.shieldsplus.module;


import com.insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.insanelib.base.Module;

public class Modules {
    public static Module BaseModule;

    public static void init() {
        BaseModule = Module.Builder.create(ShieldsPlus.RESOURCE_PREFIX + "base", "Shields Plus")
                .canBeDisabled(false)
                .build();
    }
}

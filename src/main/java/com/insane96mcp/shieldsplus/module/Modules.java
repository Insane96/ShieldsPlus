package com.insane96mcp.shieldsplus.module;


import com.insane96mcp.shieldsplus.module.base.BaseModule;

public class Modules {
    public static BaseModule base;
    public static void init() {
        base = new BaseModule();
    }

    public static void loadConfig() {
        base.loadConfig();
    }
}

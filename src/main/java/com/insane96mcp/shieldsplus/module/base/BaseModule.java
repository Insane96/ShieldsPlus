package com.insane96mcp.shieldsplus.module.base;

import com.insane96mcp.shieldsplus.module.base.feature.BaseFeature;
import com.insane96mcp.shieldsplus.setup.Config;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;

@Label(name = "Shields+")
public class BaseModule extends Module {

    public BaseFeature base;

    public BaseModule() {
        super(Config.builder, true, false);
        this.pushConfig(Config.builder);
        base = new BaseFeature(this);
        Config.builder.pop();
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        base.loadConfig();
    }
}
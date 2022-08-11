package com.insane96mcp.shieldsplus;

import com.insane96mcp.shieldsplus.setup.Client;
import com.insane96mcp.shieldsplus.setup.Config;
import com.insane96mcp.shieldsplus.setup.SPItems;
import com.insane96mcp.shieldsplus.setup.SPRecipeSerializers;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ShieldsPlus.MOD_ID)
public class ShieldsPlus
{
    public static final String MOD_ID = "shieldsplus";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ShieldsPlus()
    {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, Config.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::setup);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SPItems.ITEMS.register(modEventBus);
        SPRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
    }
}

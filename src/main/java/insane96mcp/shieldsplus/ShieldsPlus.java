package insane96mcp.shieldsplus;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import insane96mcp.insanelib.setup.ILModConfig;
import insane96mcp.shieldsplus.setup.SPDataComponents;
import insane96mcp.shieldsplus.setup.SPItems;
import insane96mcp.shieldsplus.setup.SPSoundEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;

@Mod(ShieldsPlus.MOD_ID)
public class ShieldsPlus
{
    public static final String MOD_ID = "shieldsplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ILModConfig CONFIG;

    public ShieldsPlus(IEventBus modEventBus, ModContainer modContainer) {
        CONFIG = new ILModConfig(id("main"), "Single Module", ModConfig.Type.COMMON, modEventBus, ShieldsPlus.class.getClassLoader());
        modContainer.registerConfig(ModConfig.Type.COMMON, CONFIG.spec);
        SPDataComponents.DATA_COMPONENTS.register(modEventBus);
        SPItems.ITEMS.register(modEventBus);
        SPSoundEvents.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener(this::preInit);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAddReloadListener(AddReloadListenerEvent event) {

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext context = event.getBuildContext();
    }

    public void preInit(FMLCommonSetupEvent event) {

    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Nullable
    public static ResourceLocation locationFrom(String s) {
        String[] split = s.split(":");
        if (s.contains(":"))
            return ResourceLocation.tryParse(s);
        else
            return ResourceLocation.fromNamespaceAndPath(MOD_ID, split[0]);
    }

    public static String lang(String path) {
        return MOD_ID + "." + path;
    }
}

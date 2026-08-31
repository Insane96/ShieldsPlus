package insane96mcp.shieldsplus;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import insane96mcp.insanelib.setup.ILModConfig;
import insane96mcp.shieldsplus.module.RuneCompat;
import insane96mcp.shieldsplus.module.SPRunes;
import insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import insane96mcp.shieldsplus.setup.SPDataComponents;
import insane96mcp.shieldsplus.setup.SPItems;
import insane96mcp.shieldsplus.setup.SPRecipeSerializers;
import insane96mcp.shieldsplus.setup.SPSoundEvents;
import insane96mcp.shieldsplus.setup.client.Client;
import insane96mcp.shieldsplus.world.item.enchantment.LightweightEnchantmentEffect;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
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

        modEventBus.addListener(Client::setup);
        modEventBus.addListener(Client::creativeTabsBuildContents);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ShieldBlockEntityWithoutLevelRenderer::onRegisterReloadListener);
            NeoForge.EVENT_BUS.addListener(LightweightEnchantmentEffect::onUseItemMovSpeed);
        }

        SPDataComponents.DATA_COMPONENTS.register(modEventBus);
        SPItems.ITEMS.register(modEventBus);
        SPRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        SPSoundEvents.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener(this::preInit);
        NeoForge.EVENT_BUS.register(this);

        if (RuneCompat.isLoaded())
            new SPRunes(modEventBus);
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
    public static ResourceLocation idFrom(String s) {
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

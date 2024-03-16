package insane96mcp.shieldsplus;

import insane96mcp.shieldsplus.data.ShieldDefinitionReloader;
import insane96mcp.shieldsplus.network.NetworkHandler;
import insane96mcp.shieldsplus.setup.*;
import insane96mcp.shieldsplus.setup.client.Client;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.text.DecimalFormat;

@Mod(ShieldsPlus.MOD_ID)
public class ShieldsPlus
{
    public static final String MOD_ID = "shieldsplus";
    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static DecimalFormat ONE_DECIMAL_FORMATTER;
    public ShieldsPlus()
    {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, Config.COMMON_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::creativeTabsBuildContents);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        SPItems.ITEMS.register(modEventBus);
        SPSoundEvents.SOUND_EVENTS.register(modEventBus);
        SPEnchantments.ENCHANTMENTS.register(modEventBus);
        SPRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(ShieldDefinitionReloader.INSTANCE);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NetworkHandler.init();
    }
}

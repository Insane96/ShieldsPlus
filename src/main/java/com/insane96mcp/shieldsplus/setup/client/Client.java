package com.insane96mcp.shieldsplus.setup.client;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.setup.SPItems;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ShieldsPlus.MOD_ID)
public class Client {

    @SuppressWarnings("unused")
    public static void setup(final FMLClientSetupEvent event) {
        initShields();
    }

/*    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            shieldItem.get().initClientMaterial();
            event.addSprite(shieldItem.get().clientMaterial.texture());
            event.addSprite(shieldItem.get().clientMaterialNoPattern.texture());
        }
    }*/

    private static void initShields() {
        //noinspection deprecation
        ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            ItemProperties.register(shieldItem.get(), SPShieldItem.BLOCKING, blockFn);
        }
    }

    public static void creativeTabsBuildContents(final CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == CreativeModeTabs.COMBAT) {
            for (RegistryObject<SPShieldItem> shield : SPItems.SHIELDS) {
                event.accept(shield.get());
            }
        }
    }
}

package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.item.SPShieldItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ShieldsPlus.MOD_ID)
public class Client {

    public static void setup(final FMLClientSetupEvent event) {
        initShields();
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        //TODO Check if I can loop the SPItems.SHIELDS to read all the textures from there=
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            event.addSprite(shieldItem.get().material.material.texture());
            event.addSprite(shieldItem.get().material.noPatternMaterial.texture());
        }
        /*if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            event.addSprite(SPShieldMaterials.WOODEN.material.texture());
            event.addSprite(SPShieldMaterials.WOODEN.noPatternMaterial.texture());
            event.addSprite(SPShieldMaterials.STONE.material.texture());
            event.addSprite(SPShieldMaterials.STONE.noPatternMaterial.texture());
            event.addSprite(SPShieldMaterials.GOLDEN.material.texture());
            event.addSprite(SPShieldMaterials.GOLDEN.noPatternMaterial.texture());
            event.addSprite(SPShieldMaterials.DIAMOND.material.texture());
            event.addSprite(SPShieldMaterials.DIAMOND.noPatternMaterial.texture());
            event.addSprite(SPShieldMaterials.NETHERITE.material.texture());
            event.addSprite(SPShieldMaterials.NETHERITE.noPatternMaterial.texture());
        }*/
    }

    private static void initShields() {
        ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            ItemProperties.register(shieldItem.get(), SPShieldItem.BLOCKING, blockFn);
        }
    }
}

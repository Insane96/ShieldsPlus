package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.item.SPShieldItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ShieldsPlus.MOD_ID)
public class Client {

    public static void setup(final FMLClientSetupEvent event) {
        initShields();
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            event.addSprite(SPShieldMaterials.WOODEN.texture());
            event.addSprite(SPShieldMaterials.WOODEN_NO_PATTERN.texture());
            event.addSprite(SPShieldMaterials.STONE.texture());
            event.addSprite(SPShieldMaterials.STONE_NO_PATTERN.texture());
            event.addSprite(SPShieldMaterials.GOLDEN.texture());
            event.addSprite(SPShieldMaterials.GOLDEN_NO_PATTERN.texture());
            event.addSprite(SPShieldMaterials.DIAMOND.texture());
            event.addSprite(SPShieldMaterials.DIAMOND_NO_PATTERN.texture());
            event.addSprite(SPShieldMaterials.NETHERITE.texture());
            event.addSprite(SPShieldMaterials.NETHERITE_NO_PATTERN.texture());
        }
    }

    private static void initShields() {
        //this matches up with ShieldCyclicItem where it calls startUsingItem() inside of use()
        ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        ItemProperties.register(SPItems.WOODEN_SHIELD.get(), SPShieldItem.BLOCKING, blockFn);
        ItemProperties.register(SPItems.STONE_SHIELD.get(), SPShieldItem.BLOCKING, blockFn);
        ItemProperties.register(SPItems.GOLDEN_SHIELD.get(), SPShieldItem.BLOCKING, blockFn);
        ItemProperties.register(SPItems.DIAMOND_SHIELD.get(), SPShieldItem.BLOCKING, blockFn);
        ItemProperties.register(SPItems.NETHERITE_SHIELD.get(), SPShieldItem.BLOCKING, blockFn);
    }
}

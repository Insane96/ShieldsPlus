package com.insane96mcp.shieldsplus.item;

import com.insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {

    public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");
    private final double blockedDamage;

    public SPShieldItem(double blockedDamage, Properties p_43089_) {
        super(p_43089_);
        this.blockedDamage = blockedDamage;
    }

    public double getBlockedDamage() {
        return this.blockedDamage;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }
}

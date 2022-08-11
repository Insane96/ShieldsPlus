package com.insane96mcp.shieldsplus.world.item;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import com.insane96mcp.shieldsplus.setup.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {

    public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");
    public final SPShieldMaterial material;

    @OnlyIn(Dist.CLIENT)
    public Material clientMaterial;
    @OnlyIn(Dist.CLIENT)
    public Material clientMaterialNoPattern;

    public SPShieldItem(SPShieldMaterial material, Properties p_43089_) {
        super(p_43089_);
        this.material = material;
    }

    public @NotNull String getDescriptionId(@NotNull ItemStack itemStack) {
        return super.getDescriptionId();
    }

    public double getBlockedDamage() {
        return this.material.damageBlocked;
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

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        addDamageBlockedText(components, this.getBlockedDamage());
    }

    public static void addDamageBlockedText(List<Component> components, double blockedDamage) {
        components.add(new TranslatableComponent(Strings.Translatable.DAMAGE_BLOCKED, new DecimalFormat("#.#").format(blockedDamage)).withStyle(ChatFormatting.BLUE));
    }

    @Override
    public boolean isValidRepairItem(ItemStack repaired, ItemStack repairingMaterial) {
        return super.isValidRepairItem(repaired, repairingMaterial);
    }

    @OnlyIn(Dist.CLIENT)
    public void initClientMaterial() {
        this.clientMaterial = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield".formatted(this.material.materialName)));
        this.clientMaterialNoPattern = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield_nopattern".formatted(this.material.materialName)));
    }
}

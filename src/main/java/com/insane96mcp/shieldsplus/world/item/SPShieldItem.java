package com.insane96mcp.shieldsplus.world.item;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import com.insane96mcp.shieldsplus.setup.Strings;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldReflectionEnchantment;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldReinforcedEnchantment;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
    public int getEnchantmentValue() {
        return this.material.enchantmentValue;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        addDamageBlockedText(itemStack, components, this.getBlockedDamage());
    }

    public static void addDamageBlockedText(ItemStack itemStack, List<Component> components, double blockedDamage) {
        int reinforced = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), itemStack);
        float finalBlockedDamage = (float) (blockedDamage + ShieldReinforcedEnchantment.getDamageBlocked(reinforced));
        int reflection = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REFLECTION.get(), itemStack);
        float reflectedDamage = ShieldReflectionEnchantment.getReflectedDamage(reflection);
        float blockedDamageReduction = (float) (ShieldReflectionEnchantment.getBlockedDamageReduction(reflection) * blockedDamage);
        finalBlockedDamage -= blockedDamageReduction;
        components.add(new TranslatableComponent(Strings.Translatable.DAMAGE_BLOCKED, new DecimalFormat("#.#").format(finalBlockedDamage)).withStyle(ChatFormatting.BLUE));
        if (reinforced > 0) {
            components.add(new TranslatableComponent(Strings.Translatable.REINFORCED_BONUS, new DecimalFormat("#.#").format(ShieldReinforcedEnchantment.getDamageBlocked(reinforced))).withStyle(ChatFormatting.DARK_GRAY));
        }
        if (reflection > 0) {
            components.add(new TranslatableComponent(Strings.Translatable.REFLECTION_MALUS, new DecimalFormat("#.#").format(blockedDamageReduction)).withStyle(ChatFormatting.DARK_GRAY));
        }
        //Add here more blocking damage modifiers

        if (reflection > 0) {
            components.add(new TranslatableComponent(Strings.Translatable.DAMAGE_REFLECTED, new DecimalFormat("#.#").format(reflectedDamage * 100)).withStyle(ChatFormatting.BLUE));
            components.add(new TranslatableComponent(Strings.Translatable.CAPPED_DAMAGE_REFLECTED, new DecimalFormat("#.#").format(ShieldReflectionEnchantment.getCappedReflectedDamage(reflection))).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack repaired, @NotNull ItemStack repairingMaterial) {
        if (this.material.repairItem != null)
            return repairingMaterial.is(this.material.repairItem);
        return repairingMaterial.is(this.material.repairTag);
    }

    @OnlyIn(Dist.CLIENT)
    public void initClientMaterial() {
        this.clientMaterial = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield".formatted(this.material.materialName)));
        this.clientMaterialNoPattern = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield_nopattern".formatted(this.material.materialName)));
    }
}

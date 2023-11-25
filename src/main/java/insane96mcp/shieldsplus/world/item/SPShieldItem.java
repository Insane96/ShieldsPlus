package insane96mcp.shieldsplus.world.item;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.data.ShieldDefinition;
import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.setup.client.ClientMaterials;
import insane96mcp.shieldsplus.world.item.enchantment.ReflectionEnchantment;
import insane96mcp.shieldsplus.world.item.enchantment.ReinforcedEnchantment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {
    public static final ShieldDefinition DEFAULT_SHIELD_DEFINITION = new ShieldDefinition(2f, 50, 20);

    public static final String DAMAGE_BLOCKED = ShieldsPlus.RESOURCE_PREFIX + "damage_blocked";
    public static final String BLOCKING_TIME = ShieldsPlus.RESOURCE_PREFIX + "blocking_time";
    public static final String COOLDOWN = ShieldsPlus.RESOURCE_PREFIX + "cooldown";
    public static final String DAMAGE_REFLECTED = ShieldsPlus.RESOURCE_PREFIX + "damage_reflected";
    public static final String CAPPED_DAMAGE_REFLECTED = ShieldsPlus.RESOURCE_PREFIX + "capped_damage_reflected";

    public static final EnchantmentCategory SHIELD = EnchantmentCategory.create("shield", s -> s instanceof ShieldItem);
    public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");
    public final SPShieldMaterial material;
    public ClientMaterials clientMaterials;

    public ShieldDefinition shieldDefinition = DEFAULT_SHIELD_DEFINITION;

    public SPShieldItem(SPShieldMaterial material, Properties p_43089_) {
        super(p_43089_);
        this.material = material;
    }

    public double getBlockedDamage(ItemStack stack, @Nullable LivingEntity entity, Level level) {
        return this.shieldDefinition.blockedDamage;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        if (!BaseFeature.liftedAndCooldown)
            return 72000;
        return this.shieldDefinition.blockingTicks == 0 ? 72000 : this.shieldDefinition.blockingTicks;
    }

    public int getCooldown(ItemStack stack, @Nullable LivingEntity entity, Level level) {
        return this.shieldDefinition.cooldownTicks;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (!BaseFeature.liftedAndCooldown)
            return stack;
        int cooldown = this.getCooldown(stack, livingEntity, level);
        if (cooldown > 0 && livingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(stack.getItem(), cooldown);
        }
        return stack;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (!BaseFeature.liftedAndCooldown)
            return;
        int cooldown = this.getCooldown(stack, entity, entity.level());
        if (cooldown > 0 && entity instanceof Player player) {
            player.getCooldowns().addCooldown(stack.getItem(), cooldown);
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return this.material.enchantmentValue;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        addDamageBlockedText(itemStack, components, this.getBlockedDamage(itemStack, null, level));
        int useDuration = this.getUseDuration(itemStack);
        if (useDuration < 72000) {
            components.add(Component.translatable(BLOCKING_TIME, ShieldsPlus.ONE_DECIMAL_FORMATTER.format(useDuration / 20f)).withStyle(ChatFormatting.BLUE));
            components.add(Component.translatable(COOLDOWN, ShieldsPlus.ONE_DECIMAL_FORMATTER.format(this.getCooldown(itemStack, null, level) / 20f)).withStyle(ChatFormatting.BLUE));
        }
    }

    public static void addDamageBlockedText(ItemStack itemStack, List<Component> components, double blockedDamage) {
        if (!BaseFeature.shieldBlockFixedDamageAmount)
            return;
        int reinforced = itemStack.getEnchantmentLevel(SPEnchantments.REINFORCED.get());
        float finalBlockedDamage = (float) (blockedDamage + ReinforcedEnchantment.getDamageBlocked(reinforced));
        int reflection = itemStack.getEnchantmentLevel(SPEnchantments.REFLECTION.get());
        float reflectedDamage = ReflectionEnchantment.getReflectedDamage(reflection);
        components.add(Component.translatable(DAMAGE_BLOCKED, ShieldsPlus.ONE_DECIMAL_FORMATTER.format(finalBlockedDamage)).withStyle(ChatFormatting.BLUE));

        if (reflection > 0) {
            components.add(Component.translatable(DAMAGE_REFLECTED, ShieldsPlus.ONE_DECIMAL_FORMATTER.format(reflectedDamage * 100)).withStyle(ChatFormatting.BLUE));
            components.add(Component.translatable(CAPPED_DAMAGE_REFLECTED, ShieldsPlus.ONE_DECIMAL_FORMATTER.format(ReflectionEnchantment.getCappedReflectedDamage(reflection))).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack repaired, @NotNull ItemStack repairingMaterial) {
        if (this.material.repairItem != null)
            return repairingMaterial.is(this.material.repairItem.get());
        return repairingMaterial.is(this.material.repairTag);
    }

    public void tryCacheClientMaterials() {
        if (this.clientMaterials == null) {
            this.clientMaterials = new ClientMaterials(new Material(Sheets.SHIELD_SHEET, new ResourceLocation(ForgeRegistries.ITEMS.getKey(this).getNamespace(), "entity/shield/%s_shield_nopattern".formatted(this.material.materialName))), new Material(Sheets.SHIELD_SHEET, new ResourceLocation(ForgeRegistries.ITEMS.getKey(this).getNamespace(), "entity/shield/%s_shield".formatted(this.material.materialName))));
        }
    }

    public Material getClientMaterial(boolean hasBanner) {
        tryCacheClientMaterials();
        return hasBanner ? this.clientMaterials.patternMaterial() : this.clientMaterials.noPatternMaterial();
    }
}

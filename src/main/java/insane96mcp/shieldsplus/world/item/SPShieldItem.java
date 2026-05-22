package insane96mcp.shieldsplus.world.item;

import insane96mcp.insanelib.InsaneLib;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.render.ShieldBlockEntityWithoutLevelRenderer;
import insane96mcp.shieldsplus.setup.SPDataComponents;
import insane96mcp.shieldsplus.setup.client.ClientMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SPShieldItem extends ShieldItem {
    public static final String DAMAGE_BLOCKED = ShieldsPlus.lang("damage_blocked");
    public static final String BLOCKING_TIME = ShieldsPlus.lang("blocking_time");
    public static final String COOLDOWN = ShieldsPlus.lang("cooldown");

    public final Ingredient repairIngredient;
    public ClientMaterials clientMaterials;

    public SPShieldItem(Properties properties, float damageBlocked, float blockingTime, float cooldown, Ingredient repairIngredient) {
        super(properties
                .component(SPDataComponents.BLOCKED_DAMAGE.get(), damageBlocked)
                .component(SPDataComponents.BLOCKING_TIME.get(), blockingTime)
                .component(SPDataComponents.COOLDOWN.get(), cooldown)
        );
        this.repairIngredient = repairIngredient;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.repairIngredient.test(repair);
    }

    public static float getBlockedDamage(ItemStack stack) {
        if (!SPFeature.shieldBlockFixedDamageAmount)
            return Float.MAX_VALUE;
        return stack.getOrDefault(SPDataComponents.BLOCKED_DAMAGE, 4f);
    }

    public static float getBlockingTime(ItemStack stack) {
        if (!SPFeature.liftedAndCooldown)
            return 3600f;
        return stack.getOrDefault(SPDataComponents.BLOCKING_TIME, 3f);
    }

    public static float getCooldown(ItemStack stack) {
        return stack.getOrDefault(SPDataComponents.COOLDOWN, 2f);
    }

    public float calculateCooldownFromTimeLifted(ItemStack stack, LivingEntity entity, int timeLifted) {
        float maxCooldown = getCooldown(stack);
        float cooldown = maxCooldown;
        float blockingTime = getBlockingTime(stack) * 20;
        timeLifted = this.getUseDuration(stack, entity) - timeLifted;
        cooldown *= ((float)timeLifted / blockingTime);
        if (cooldown / maxCooldown < SPFeature.minCooldown)
            cooldown = maxCooldown * SPFeature.minCooldown.floatValue();
        //TODO
        //int fastRecovery = stack.getEnchantmentLevel(SPEnchantments.FAST_RECOVERY.get());
        //if (fastRecovery > 0)
        //    cooldown = (int) (cooldown * (1f - SPFeature.enchantments$fastRecoveryCooldownReduction));
        return cooldown;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (!SPFeature.liftedAndCooldown)
            return 72000;
        return (int) (getBlockingTime(stack) * 20);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        return tryApplyCooldown(stack, livingEntity);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        tryApplyCooldown(stack, entity);
    }

    public ItemStack tryApplyCooldown(ItemStack stack, LivingEntity entity) {
        if (!SPFeature.liftedAndCooldown)
            return stack;
        float cooldown = this.calculateCooldownFromTimeLifted(stack, entity, entity.getUseItemRemainingTicks());
        if (cooldown > 0 && entity instanceof Player player)
            player.getCooldowns().addCooldown(stack.getItem(), (int) (cooldown * 20f));
        return stack;
    }

    //TODO
    /*@Override
    public int getEnchantmentValue(ItemStack stack) {
        return this.material.enchantmentValue;
    }*/

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        float blockingTime = getBlockingTime(stack);
        if (blockingTime < 3600f && SPFeature.liftedAndCooldown) {
            tooltipComponents.add(Component.translatable(BLOCKING_TIME, InsaneLib.ONE_DECIMAL_FORMATTER.format(blockingTime)).withStyle(ChatFormatting.BLUE));
            tooltipComponents.add(Component.translatable(COOLDOWN, InsaneLib.ONE_DECIMAL_FORMATTER.format(getCooldown(stack))).withStyle(ChatFormatting.BLUE));
        }
        if (SPFeature.shieldBlockFixedDamageAmount) {
            tooltipComponents.add(Component.translatable(DAMAGE_BLOCKED, InsaneLib.ONE_DECIMAL_FORMATTER.format(getBlockedDamage(stack))).withStyle(ChatFormatting.BLUE));
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

    public void tryCacheClientMaterials() {
        if (this.clientMaterials == null) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(this);
            this.clientMaterials = new ClientMaterials(
                new Material(Sheets.SHIELD_SHEET, ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "entity/shield/" + key.getPath() + "_nopattern")),
                new Material(Sheets.SHIELD_SHEET, ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "entity/shield/" + key.getPath()))
            );
        }
    }

    public Material getClientMaterial(boolean hasBanner) {
        tryCacheClientMaterials();
        return hasBanner ? this.clientMaterials.patternMaterial() : this.clientMaterials.noPatternMaterial();
    }
}

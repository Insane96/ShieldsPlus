package com.insane96mcp.shieldsplus.module.base.feature;

import com.insane96mcp.shieldsplus.setup.Config;
import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import com.insane96mcp.shieldsplus.setup.SPShieldMaterials;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Label(name = "Shields+")
public class BaseFeature extends Feature {

    private final ForgeConfigSpec.BooleanValue removeShieldWindupConfig;
    private final ForgeConfigSpec.BooleanValue shieldBlockFixedDamageAmountConfig;
    private final ForgeConfigSpec.DoubleValue minShieldHurtDamageConfig;
    private final ForgeConfigSpec.BooleanValue combatTestShieldDisablingConfig;

    public boolean removeShieldWindup = true;
    public boolean shieldBlockFixedDamageAmount = true;
    public double minShieldHurtDamage = 0f;
    public boolean combatTestShieldDisabling = true;

    public BaseFeature(Module module) {
        super(Config.builder, module, true, false);
        //super.pushConfig(Config.builder);
        this.removeShieldWindupConfig = Config.builder
                .comment("In vanilla when you start blocking with a shield, there's a 0.25 seconds window where you are still not blocking. If true this (stupid) windup time is removed.")
                .define("Remove Shield Windup", this.removeShieldWindup);
        this.shieldBlockFixedDamageAmountConfig = Config.builder
                .comment("If true shields will block only a certain amount of damage. If false the vanilla behaviour is used.")
                .define("Shields Block Fixed Damage Amount", this.shieldBlockFixedDamageAmount);
        this.minShieldHurtDamageConfig = Config.builder
                .comment("The minimum damage dealt to the player for the shield to take damage (reduce durability). Vanilla is 3")
                .defineInRange("Min Shield Hurt Damage", this.minShieldHurtDamage, 0f, Float.MAX_VALUE);
        combatTestShieldDisablingConfig = Config.builder
                .comment("Makes shields always disable for 1.6 seconds like Combat Test snapshots.")
                .define("Combat Test shield disabling", this.combatTestShieldDisabling);
        //Config.builder.pop();
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        this.removeShieldWindup = this.removeShieldWindupConfig.get();
        this.shieldBlockFixedDamageAmount = this.shieldBlockFixedDamageAmountConfig.get();
        this.minShieldHurtDamage = this.minShieldHurtDamageConfig.get();
        this.combatTestShieldDisabling = this.combatTestShieldDisablingConfig.get();
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        if (!this.isEnabled()
                || !this.shieldBlockFixedDamageAmount)
            return;

        double blockedDamage;
        if (event.getEntityLiving().getUseItem().is(Items.SHIELD)) {
            blockedDamage = SPShieldMaterials.IRON.damageBlocked;
        }
        else if (event.getEntityLiving().getUseItem().getItem() instanceof SPShieldItem) {
            blockedDamage = ((SPShieldItem)event.getEntityLiving().getUseItem().getItem()).getBlockedDamage();
        }
        else
            return;

        int reinforced = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), event.getEntityLiving().getUseItem());
        blockedDamage += reinforced * 0.5d;

        event.setBlockedDamage((float) blockedDamage);

        processEnchantments(event.getEntityLiving(), event.getDamageSource());
    }

    private void processEnchantments(LivingEntity blockingEntity, DamageSource source) {
        if (blockingEntity.getUseItem().getItem() instanceof ShieldItem) {
            ItemStack shield = blockingEntity.getUseItem();
            int recoil = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.RECOIL.get(), shield);
            if (recoil > 0)
            {
                if (source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()) {
                    sourceEntity.knockback(recoil * 0.4d, blockingEntity.getX() - sourceEntity.getX(), blockingEntity.getZ() - sourceEntity.getZ());
                }
                else if (source.getDirectEntity() instanceof Projectile projectile) {
                    projectile.setDeltaMovement(projectile.getDeltaMovement().scale(recoil * 3.5d));
                }
            }

            int pointed = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.POINTED.get(), shield);
            if (pointed > 0 && source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity())
            {
                sourceEntity.hurt(DamageSource.mobAttack(blockingEntity), pointed);
            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!this.isEnabled()
                || !this.shieldBlockFixedDamageAmount)
            return;

        if (event.getItemStack().is(Items.SHIELD)) {
            SPShieldItem.addDamageBlockedText(event.getItemStack(), event.getToolTip(), SPShieldMaterials.IRON.damageBlocked);
        }
    }

    public boolean shouldRemoveShieldWindup() {
        return this.isEnabled() && this.removeShieldWindup;
    }

    public boolean combatTestShieldDisabling() {
        return this.isEnabled() && this.combatTestShieldDisabling;
    }
}
package com.insane96mcp.shieldsplus.module.base.feature;

import com.insane96mcp.shieldsplus.item.SPShieldItem;
import com.insane96mcp.shieldsplus.setup.Config;
import com.insane96mcp.shieldsplus.setup.SPShieldMaterials;
import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Label(name = "Shields+")
public class BaseFeature extends Feature {

    private final ForgeConfigSpec.BooleanValue removeShieldWindupConfig;
    private final ForgeConfigSpec.BooleanValue shieldBlockFixedDamageAmountConfig;
    private final ForgeConfigSpec.DoubleValue minShieldHurtDamageConfig;

    public boolean removeShieldWindup = true;
    public boolean shieldBlockFixedDamageAmount = true;
    public double minShieldHurtDamage = 0f;

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
        //Config.builder.pop();
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        this.removeShieldWindup = this.removeShieldWindupConfig.get();
        this.shieldBlockFixedDamageAmount = this.shieldBlockFixedDamageAmountConfig.get();
        this.minShieldHurtDamage = this.minShieldHurtDamageConfig.get();
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

        event.setBlockedDamage((float) blockedDamage);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!this.isEnabled()
                || !this.shieldBlockFixedDamageAmount)
            return;

        if (event.getItemStack().is(Items.SHIELD)) {
            SPShieldItem.addDamageBlockedText(event.getToolTip(), SPShieldMaterials.IRON.damageBlocked);
        }
    }

    public boolean shouldRemoveShieldWindup() {
        return this.isEnabled() && this.removeShieldWindup;
    }
}
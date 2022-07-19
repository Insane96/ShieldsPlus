package com.insane96mcp.shieldsplus.module.base.feature;

import com.insane96mcp.shieldsplus.item.SPShieldItem;
import com.insane96mcp.shieldsplus.setup.Config;
import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Label(name = "Shields+")
public class BaseFeature extends Feature {

    private final ForgeConfigSpec.BooleanValue shieldBlockFixedDamageAmountConfig;

    public boolean shieldBlockFixedDamageAmount = true;

    public BaseFeature(Module module) {
        super(Config.builder, module, true, false);
        //super.pushConfig(Config.builder);
        this.shieldBlockFixedDamageAmountConfig = Config.builder
                .comment("If true shields will block only a certain amount of damage. If false the vanilla behaviour is used.")
                .define("Shields Block Fixed Damage Amount", this.shieldBlockFixedDamageAmount);
        //Config.builder.pop();
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
        this.shieldBlockFixedDamageAmount = this.shieldBlockFixedDamageAmountConfig.get();
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        if (!this.isEnabled()
                || !this.shieldBlockFixedDamageAmount)
            return;

        double blockedDamage;
        if (event.getEntityLiving().getUseItem().is(Items.SHIELD)) {
            blockedDamage = 5d;
        }
        else if (event.getEntityLiving().getUseItem().getItem() instanceof SPShieldItem) {
            blockedDamage = ((SPShieldItem)event.getEntityLiving().getUseItem().getItem()).getBlockedDamage();
        }
        else
            return;

        event.setBlockedDamage((float) blockedDamage);
    }
}
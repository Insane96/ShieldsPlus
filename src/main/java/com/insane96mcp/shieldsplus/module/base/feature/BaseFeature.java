package com.insane96mcp.shieldsplus.module.base.feature;

import com.insane96mcp.shieldsplus.item.SPShieldItem;
import com.insane96mcp.shieldsplus.setup.Config;
import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

@Label(name = "Shields+")
public class BaseFeature extends Feature {

    //private final ForgeConfigSpec.ConfigValue<Integer> startingHealthConfig;

    //public int startingHealth = 20;

    public BaseFeature(Module module) {
        super(Config.builder, module, true, false);
        super.pushConfig(Config.builder);

        Config.builder.pop();
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        if (!this.isEnabled())
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
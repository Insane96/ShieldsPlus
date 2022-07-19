package com.insane96mcp.shieldsplus.mixin;

import com.insane96mcp.shieldsplus.module.Modules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @ModifyConstant(constant = @Constant(floatValue = 3.0F), method = "hurtCurrentlyUsedShield")
    private float blockingWindupTime(float minDamage) {
        return (float) Modules.base.base.minShieldHurtDamage;
    }
}
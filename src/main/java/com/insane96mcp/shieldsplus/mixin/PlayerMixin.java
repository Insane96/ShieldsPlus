package com.insane96mcp.shieldsplus.mixin;

import com.insane96mcp.shieldsplus.module.Modules;
import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldFastRecoveryEnchantment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyConstant(constant = @Constant(floatValue = 3.0F), method = "hurtCurrentlyUsedShield")
    private float blockingWindupTime(float minDamage) {
        return (float) Modules.base.base.minShieldHurtDamage;
    }

    @Inject(method = "disableShield", at = @At("HEAD"), cancellable = true)
    private void disableShield(boolean efficiencyAffected, CallbackInfo callbackInfo) {
        if (Modules.base.base.combatTestShieldDisabling()) {
            callbackInfo.cancel();
            int ticks = 32;
            int fastRecovery = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.FAST_RECOVERY.get(), this.getUseItem());
            this.getCooldowns().addCooldown(this.getUseItem().getItem(), ticks - (fastRecovery * ShieldFastRecoveryEnchantment.TICKS));
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }
    }

    @Shadow
    public abstract ItemCooldowns getCooldowns();
}
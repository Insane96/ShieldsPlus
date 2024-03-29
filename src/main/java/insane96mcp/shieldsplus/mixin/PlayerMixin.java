package insane96mcp.shieldsplus.mixin;

import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.enchantment.FastRecoveryEnchantment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
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

    @Shadow public abstract void tick();

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyConstant(constant = @Constant(floatValue = 3.0F), method = "hurtCurrentlyUsedShield")
    private float blockingWindupTime(float minDamage) {
        return BaseFeature.minShieldHurtDamage.floatValue();
    }

    @Inject(method = "disableShield", at = @At("HEAD"), cancellable = true)
    private void disableShield(boolean efficiencyAffected, CallbackInfo callbackInfo) {
        if (BaseFeature.combatTestShieldDisabling()) {
            callbackInfo.cancel();
            int ticks = 32;
            int fastRecovery = this.getUseItem().getEnchantmentLevel(SPEnchantments.FAST_RECOVERY.get());
            if (fastRecovery > 0)
                ticks = (int) (ticks * (1 - FastRecoveryEnchantment.getCooldownReduction(fastRecovery)));
            this.getCooldowns().addCooldown(this.getUseItem().getItem(), ticks);
            this.stopUsingItem();
            this.level().broadcastEntityEvent(this, (byte)30);
        }
    }

    @Shadow
    public abstract ItemCooldowns getCooldowns();
}
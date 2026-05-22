package insane96mcp.shieldsplus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import insane96mcp.shieldsplus.module.SPFeature;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyExpressionValue(method = "hurtCurrentlyUsedShield", at = @At(value = "CONSTANT", args = "floatValue=3.0"))
    private float shieldsPlus$blockingWindupTime(float minDamage) {
        return SPFeature.minShieldHurtDamage.floatValue();
    }

    @ModifyExpressionValue(method = "disableShield", at = @At(value = "CONSTANT", args = "intValue=100"))
    private int shieldsPlus$disableShield(int original) {
        if (!SPFeature.combatTestShieldDisabling())
            return original;

        int ticks = 32;
        //TODO
        //int fastRecovery = this.getUseItem().getEnchantmentLevel(SPEnchantments.FAST_RECOVERY.get());
        //if (fastRecovery > 0)
        //    ticks = (int) (ticks * (1 - FastRecoveryEnchantment.getCooldownReduction(fastRecovery)));
        return ticks;
    }
}
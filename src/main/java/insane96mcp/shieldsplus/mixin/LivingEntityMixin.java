package insane96mcp.shieldsplus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import insane96mcp.shieldsplus.module.BaseFeature;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected abstract void playHurtSound(DamageSource p_21160_);

    @Shadow public abstract ItemStack getOffhandItem();

    @Shadow public abstract void remove(RemovalReason pReason);

    @Shadow protected ItemStack useItem;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @ModifyExpressionValue(method = "isBlocking", at = @At(value = "CONSTANT", args = "intValue=5"))
    private int shieldsPlus$blockingWindupTime(int ticks) {
        return BaseFeature.shouldRemoveShieldWindup() ? BaseFeature.shieldWindup : ticks;
    }

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    public void shieldsPlus$isBlocking(CallbackInfoReturnable<Boolean> cir) {
        if (!BaseFeature.canBlockWithCrouch((LivingEntity) (Object) this))
            return;

        cir.setReturnValue(true);
    }

    @Inject(method = "releaseUsingItem", at = @At("HEAD"), cancellable = true)
    public void shieldsPlus$onReleaseUsing(CallbackInfo ci) {
        if (!BaseFeature.canBlockWithCrouch((LivingEntity) (Object) this)
                || !this.useItem.canPerformAction(ToolActions.SHIELD_BLOCK))
            return;

        ci.cancel();
    }

    @Unique
    private boolean shieldsPlus$hasBlocked;

    @Definition(id = "flag", local = @Local(type = boolean.class, ordinal = 0))
    @Expression("flag")
    @ModifyExpressionValue(method = "hurt", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onHurt(boolean original, DamageSource source, float amount) {
        if (shieldsPlus$hasBlocked && amount > 0) {
            this.level().broadcastEntityEvent(this, (byte)29);
        }
        shieldsPlus$hasBlocked = false;
        return original;
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/ShieldBlockEvent;getBlockedDamage()F", ordinal = 1, shift = At.Shift.AFTER))
    private void shieldsPlus$onBlocked(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        this.shieldsPlus$hasBlocked = true;
    }

    @WrapOperation(method = "blockUsingShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockedByShield(Lnet/minecraft/world/entity/LivingEntity;)V"))
    public void shieldsPlus$onBlockUsingShield(LivingEntity instance, LivingEntity pDefender, Operation<Void> original, LivingEntity pAttacker) {
        pAttacker.knockback(0.5D, pDefender.getX() - pAttacker.getX(), pDefender.getZ() - pAttacker.getZ());
        if (pAttacker instanceof Player)
            pAttacker.hurtMarked = true;
        original.call(pAttacker, pDefender);
    }
}
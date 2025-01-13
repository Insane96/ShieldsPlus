package insane96mcp.shieldsplus.mixin;

import insane96mcp.shieldsplus.module.BaseFeature;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected abstract void playHurtSound(DamageSource p_21160_);

    @Shadow public abstract ItemStack getOffhandItem();

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int blockingWindupTime(int ticks) {
        return BaseFeature.shouldRemoveShieldWindup() ? BaseFeature.shieldWindup : ticks;
    }

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    public void isBlocking(CallbackInfoReturnable<Boolean> cir) {
        if (!BaseFeature.blockWithCrouch((LivingEntity) (Object) this))
            return;

        cir.setReturnValue(true);
    }

    @Inject(method = "releaseUsingItem", at = @At("HEAD"), cancellable = true)
    public void onReleaseUsing(CallbackInfo ci) {
        if (!BaseFeature.blockWithCrouch((LivingEntity) (Object) this))
            return;

        ci.cancel();
    }

    //TODO It's missing the amount of damage taken
    /*@Unique private boolean hasBlocked;
    @Unique private float amount;

    @ModifyVariable(at = @At(value = "STORE", ordinal = 1), method = "hurt", ordinal = 0)
    private boolean onBlockedFlagSet(boolean hasBlocked) {
        this.hasBlocked = hasBlocked;
        return hasBlocked;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)V"), method = "hurt")
    private void onHurtSound(LivingEntity instance, DamageSource damageSource) {
        if (hasBlocked || amount == 0f)
            return;

        this.playHurtSound(damageSource);
    }*/
}
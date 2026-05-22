package insane96mcp.shieldsplus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import insane96mcp.shieldsplus.module.SPFeature;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity {
    @Shadow public abstract InteractionHand getUsedItemHand();

    @Shadow public Input input;

    protected LocalPlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Definition(id = "isUsingItem", method = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")
    @Expression("this.isUsingItem()")
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onUsingItemSlowdown(boolean original) {
        if (original && SPFeature.canBlockWithCrouch(this))
            return false;
        return original;
    }

    @Definition(id = "stopUsingItem", method = "Lnet/minecraft/client/player/LocalPlayer;stopUsingItem()V")
    @Expression("this.stopUsingItem()")
    @WrapOperation(method = "onSyncedDataUpdated", at = @At("MIXINEXTRAS:EXPRESSION"))
    public void shieldsPlus$onUsingItemStop(LocalPlayer instance, Operation<Void> original, @Local InteractionHand interactionHand) {
        if (interactionHand != this.getUsedItemHand() && this.useItem.canPerformAction(ItemAbilities.SHIELD_BLOCK))
            return;
        original.call(instance);
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "CONSTANT", args = "floatValue=0.2f"))
    public float onUsingItemSlowdown(float original) {
        if (this.isBlocking())
            return 0.3f;
        return original;
    }
}

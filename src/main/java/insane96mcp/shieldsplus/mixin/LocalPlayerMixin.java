package insane96mcp.shieldsplus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import insane96mcp.shieldsplus.module.BaseFeature;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity {
    protected LocalPlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Definition(id = "isUsingItem", method = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")
    @Expression("this.isUsingItem()")
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onUsingItemSlowdown(boolean original) {
        if (original && BaseFeature.blockWithCrouch(this))
            return false;
        return original;
    }
}

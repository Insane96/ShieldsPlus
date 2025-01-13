package insane96mcp.shieldsplus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import insane96mcp.shieldsplus.module.BaseFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public MultiPlayerGameMode gameMode;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow protected abstract boolean startAttack();

    @Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
    @Definition(id = "keyAttack", field = "Lnet/minecraft/client/Options;keyAttack:Lnet/minecraft/client/KeyMapping;")
    @Definition(id = "consumeClick", method = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    @Expression("this.options.keyAttack.consumeClick()")
    @ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onConsumeAttackClick(boolean original, @Local LocalBooleanRef flag2) {
        if (original && BaseFeature.blockWithCrouch(this.player)) {
            //this.gameMode.releaseUsingItem(this.player);
            flag2.set(flag2.get() | this.startAttack());
        }
        return original;
    }
}

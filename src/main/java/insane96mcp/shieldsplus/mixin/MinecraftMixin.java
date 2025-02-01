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

    @Shadow protected abstract void startUseItem();

    @Shadow protected abstract void pickBlock();

    @Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
    @Definition(id = "keyAttack", field = "Lnet/minecraft/client/Options;keyAttack:Lnet/minecraft/client/KeyMapping;")
    @Definition(id = "consumeClick", method = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    @Expression("this.options.keyAttack.consumeClick()")
    @ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onConsumeAttackClickWhenUsingItem(boolean original, @Local LocalBooleanRef flag2) {
        if (original && BaseFeature.canBlockWithCrouch(this.player)) {
            //this.gameMode.releaseUsingItem(this.player);
            flag2.set(flag2.get() | this.startAttack());
        }
        return original;
    }

    @Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
    @Definition(id = "keyUse", field = "Lnet/minecraft/client/Options;keyUse:Lnet/minecraft/client/KeyMapping;")
    @Definition(id = "consumeClick", method = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    @Expression("this.options.keyUse.consumeClick()")
    @ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onConsumeUseClickWhenUsingItem(boolean original, @Local LocalBooleanRef flag2) {
        if (original && BaseFeature.canBlockWithCrouch(this.player)) {
            this.startUseItem();
        }
        return original;
    }

    @Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
    @Definition(id = "keyPickItem", field = "Lnet/minecraft/client/Options;keyPickItem:Lnet/minecraft/client/KeyMapping;")
    @Definition(id = "consumeClick", method = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    @Expression("this.options.keyPickItem.consumeClick()")
    @ModifyExpressionValue(method = "handleKeybinds", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public boolean shieldsPlus$onConsumePickItemClickWhenUsingItem(boolean original, @Local LocalBooleanRef flag2) {
        if (original && BaseFeature.canBlockWithCrouch(this.player)) {
            this.pickBlock();
        }
        return original;
    }

    @Definition(id = "player", field = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;")
    @Definition(id = "isUsingItem", method = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")
    @Expression("this.player.isUsingItem()")
    @ModifyExpressionValue(method = "continueAttack", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public boolean shieldsPlus$onUsingItemCheckWhenContinueAttack(boolean original) {
        if (!original)
            return false;
        return !BaseFeature.canBlockWithCrouch(this.player);
    }
}

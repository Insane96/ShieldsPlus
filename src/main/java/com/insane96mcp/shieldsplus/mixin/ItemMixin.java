package com.insane96mcp.shieldsplus.mixin;

import com.insane96mcp.shieldsplus.setup.SPShieldMaterials;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(at = @At("RETURN"), method = "getEnchantmentValue", cancellable = true)
    public void getEnchantmentValue(CallbackInfoReturnable<Integer> callbackInfo) {
        //noinspection ConstantConditions
        if (((Object)this) instanceof ShieldItem
                && !(((Object)this) instanceof SPShieldItem))
            callbackInfo.setReturnValue(SPShieldMaterials.IRON.enchantmentValue);
    }
}
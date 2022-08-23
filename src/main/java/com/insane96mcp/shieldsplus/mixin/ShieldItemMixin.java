package com.insane96mcp.shieldsplus.mixin;

import com.insane96mcp.shieldsplus.setup.SPShieldMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {

    public ShieldItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
    public void isValidRepairItem(ItemStack p_43091_, ItemStack repairingMaterial, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (SPShieldMaterials.IRON.repairItem != null) {
            callbackInfo.setReturnValue(repairingMaterial.is(SPShieldMaterials.IRON.repairItem));
            return;
        }
        callbackInfo.setReturnValue(repairingMaterial.is(SPShieldMaterials.IRON.repairTag));
    }

    @Inject(at = @At("RETURN"), method = "getDescriptionId", cancellable = true)
    public void getDescriptionId(ItemStack stack, CallbackInfoReturnable<String> callbackInfo) {
        callbackInfo.setReturnValue(super.getDescriptionId());
    }

    @Inject(at = @At("RETURN"), method = "getEnchantmentValue", cancellable = true)
    public void getItemEnchantability(CallbackInfoReturnable<Integer> callbackInfo) {
        callbackInfo.setReturnValue(5);
    }
}
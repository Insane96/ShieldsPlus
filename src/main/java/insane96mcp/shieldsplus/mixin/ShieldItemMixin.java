package insane96mcp.shieldsplus.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {

    public ShieldItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    /*@Inject(at = @At("RETURN"), method = "isValidRepairItem", cancellable = true)
    public void isValidRepairItem(ItemStack p_43091_, ItemStack repairingMaterial, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (SPShieldMaterials.IRON.repairItem != null) {
            callbackInfo.setReturnValue(repairingMaterial.is(SPShieldMaterials.IRON.repairItem.get()));
            return;
        }
        callbackInfo.setReturnValue(repairingMaterial.is(SPShieldMaterials.IRON.repairTag));
    }*/
}
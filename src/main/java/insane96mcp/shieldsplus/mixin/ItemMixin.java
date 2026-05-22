package insane96mcp.shieldsplus.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin {

    //TODO
    /*@Inject(at = @At("RETURN"), method = "getEnchantmentValue", cancellable = true)
    public void shieldsPlus$getEnchantmentValue(CallbackInfoReturnable<Integer> callbackInfo) {
        //noinspection ConstantConditions
        if (((Object)this) instanceof ShieldItem
                && !(((Object)this) instanceof SPShieldItem))
            callbackInfo.setReturnValue(SPShieldMaterials.IRON.enchantmentValue);
    }*/
}
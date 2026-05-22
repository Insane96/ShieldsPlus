package insane96mcp.shieldsplus.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fired when the player tried to block when crouching. Can be canceled to prevent blocking
 */
public class BlockWithCrouchEvent extends LivingEvent implements ICancellableEvent {
    ItemStack stack;
    public BlockWithCrouchEvent(LivingEntity entity, ItemStack stack) {
        super(entity);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}

package insane96mcp.shieldsplus.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired when the player tried to block when crouching. Can be canceled to prevent blocking
 */
@Cancelable
public class BlockWithCrouchEvent extends LivingEvent {
    ItemStack stack;
    public BlockWithCrouchEvent(LivingEntity entity, ItemStack stack) {
        super(entity);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}

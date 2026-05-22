package insane96mcp.shieldsplus.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

public class SPEventFactory {
    /**
     * Returns true if the player can block while crouching
     */
    public static boolean canBlockWithCrouch(LivingEntity entity, ItemStack stack) {
        BlockWithCrouchEvent event = new BlockWithCrouchEvent(entity, stack);
        NeoForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }
}

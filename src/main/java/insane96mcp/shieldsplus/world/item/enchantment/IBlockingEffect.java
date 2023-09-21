package insane96mcp.shieldsplus.world.item.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IBlockingEffect {
    void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level);
}

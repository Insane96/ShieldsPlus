package insane96mcp.shieldsplus.world.item.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public interface IBlockingEnchantmentEffect {
    void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event);
}

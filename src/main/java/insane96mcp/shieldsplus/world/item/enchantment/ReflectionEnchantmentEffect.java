package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.SPFeature;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class ReflectionEnchantmentEffect implements IBlockingEnchantmentEffect {
    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        if (!(source.getEntity() instanceof LivingEntity sourceEntity) || source.getEntity() != source.getDirectEntity())
            return;

        float reflectedDamage = level * SPFeature.enchantments$reflectionReflectedDamage.floatValue();
        sourceEntity.hurt(blockingEntity.damageSources().thorns(blockingEntity), reflectedDamage * amount);
    }
}

package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.SPFeature;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class RecoilEnchantmentEffect implements IBlockingEnchantmentEffect {
    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        if (source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()) {
            sourceEntity.knockback(level * SPFeature.enchantments$recoilEntitiesKnockback, blockingEntity.getX() - sourceEntity.getX(), blockingEntity.getZ() - sourceEntity.getZ());
            if (sourceEntity instanceof Player)
                sourceEntity.hurtMarked = true;
        }
        else if (source.getDirectEntity() instanceof Projectile projectile)
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(level * SPFeature.enchantments$recoilProjectilesKnockback));
    }
}

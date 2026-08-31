package insane96mcp.shieldsplus.world.item.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Replicates vanilla Thorns (chance = 0.15 per level, 1-5 damage, 2 durability to the enchanted item) since
 * vanilla's data-driven effect can't be scoped to only trigger while actively blocking with a shield.
 */
public class ThornsEnchantmentEffect implements IBlockingEnchantmentEffect {
    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        if (!(source.getEntity() instanceof LivingEntity sourceEntity) || source.getEntity() != source.getDirectEntity())
            return;

        if (blockingEntity.getRandom().nextFloat() >= 0.15f * level)
            return;

        float thornsDamage = Mth.randomBetween(blockingEntity.getRandom(), 1f, 5f);
        sourceEntity.hurt(blockingEntity.damageSources().thorns(blockingEntity), thornsDamage);

        EquipmentSlot slot = blockingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        blockingEntity.getUseItem().hurtAndBreak(2, blockingEntity, slot);
    }
}

package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Blocking (see {@link #onBlocked}) arms a save that's consumed on the next lethal blow: the armed amount
 * is captured in {@link #trySaveAmount} from {@link insane96mcp.shieldsplus.module.SPFeature#onLivingDamage},
 * and actually applied in {@link #tryApply} from {@code onLivingDeath}. State is tracked in the entity's
 * persistent data since it has to survive between three separate events.
 */
public class CelestialGuardianEnchantmentEffect implements IBlockingEnchantmentEffect {
    public static final String LAST_TRIGGER_TAG = ShieldsPlus.MOD_ID + ":celestial_guardian_last_trigger";
    public static final String SHOULD_APPLY_TAG = ShieldsPlus.MOD_ID + ":celestial_guardian";

    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        if (blockingEntity.getPersistentData().getLong(LAST_TRIGGER_TAG) + 2 * 60 * 20 > blockingEntity.level().getGameTime())
            return;

        blockingEntity.getPersistentData().putBoolean(SHOULD_APPLY_TAG, true);
        blockingEntity.getPersistentData().putLong(LAST_TRIGGER_TAG, blockingEntity.level().getGameTime());
    }

    public static void trySaveAmount(LivingEntity entity, float amount) {
        if (!entity.getPersistentData().contains(SHOULD_APPLY_TAG))
            return;

        entity.getPersistentData().putFloat(SHOULD_APPLY_TAG, amount);
    }

    public static boolean tryApply(LivingEntity entity) {
        if (!entity.isBlocking() || !entity.getPersistentData().contains(SHOULD_APPLY_TAG))
            return false;

        float amount = entity.getPersistentData().getFloat(SHOULD_APPLY_TAG);
        entity.getPersistentData().remove(SHOULD_APPLY_TAG);
        float health = entity.getHealth();
        if ((health - amount) / entity.getMaxHealth() > 0.25f)
            return false;

        float maxAmount = Math.min(amount, health);
        health -= maxAmount;
        amount -= maxAmount;
        entity.level().playSound(null, entity, SPSoundEvents.CELESTIAL_GUARDIAN.get(), entity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1f, 1f);
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20, 4));
        if (health <= 0f && amount < 20f) {
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() - amount);
            return true;
        }
        return false;
    }
}

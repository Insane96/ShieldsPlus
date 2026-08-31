package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.core.ModNBTData;
import insane96mcp.shieldsplus.setup.SPSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Totem-of-Undying-like save: blocking arms it, and a would-be-lethal blow while armed can cancel death.
 * State is tracked in the entity's persistent data (via {@link ModNBTData}) since it has to survive across
 * three separate events:
 * <ol>
 *     <li>{@link #onBlocked} arms the save, gated by a {@value #REARM_COOLDOWN_TICKS}-tick cooldown so it
 *     can't be re-armed right after firing.</li>
 *     <li>{@link #trackIncomingDamage}, called from {@code SPFeature#onLivingDamage}, keeps overwriting the
 *     armed amount with the latest hit taken while armed, so it always holds the most recent damage instance.</li>
 *     <li>{@link #tryConsumeSave}, called from {@code SPFeature#onLivingDeath}, spends the armed save (whether
 *     or not it actually cancels death) using the last tracked amount.</li>
 * </ol>
 */
public class CelestialGuardianEnchantmentEffect implements IBlockingEnchantmentEffect {
    private static final int REARM_COOLDOWN_TICKS = 2 * 60 * 20;
    /** Death is only canceled if the lethal blow wouldn't have left the entity above this much of its max health. */
    private static final float MAX_HEALTH_PERCENT_TO_SAVE = 0.25f;
    private static final float ABSORPTION_OVERFLOW_CAP = 20f;

    public static ResourceLocation LAST_ARMED_AT;
    public static ResourceLocation ARMED_DAMAGE_AMOUNT;

    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        long lastArmedAt = ModNBTData.get(blockingEntity, LAST_ARMED_AT, Long.class);
        if (lastArmedAt + REARM_COOLDOWN_TICKS > blockingEntity.level().getGameTime())
            return;

        ModNBTData.put(blockingEntity, ARMED_DAMAGE_AMOUNT, amount);
        ModNBTData.put(blockingEntity, LAST_ARMED_AT, blockingEntity.level().getGameTime());
    }

    public static void trackIncomingDamage(LivingEntity entity, float amount) {
        if (!ModNBTData.contains(entity, ARMED_DAMAGE_AMOUNT))
            return;

        ModNBTData.put(entity, ARMED_DAMAGE_AMOUNT, amount);
    }

    public static boolean tryConsumeSave(LivingEntity entity) {
        if (!entity.isBlocking() || !ModNBTData.contains(entity, ARMED_DAMAGE_AMOUNT))
            return false;

        float lethalDamage = ModNBTData.get(entity, ARMED_DAMAGE_AMOUNT, Float.class);
        ModNBTData.remove(entity, ARMED_DAMAGE_AMOUNT);

        // entity.getHealth() is already the post-damage value (LivingDeathEvent fires after the killing blow
        // has been applied), so add the lethal damage back to reconstruct health just before that blow.
        float healthBeforeHit = entity.getHealth() + lethalDamage;
        if (healthBeforeHit / entity.getMaxHealth() > MAX_HEALTH_PERCENT_TO_SAVE)
            return false;

        float healthRestored = Math.min(lethalDamage, healthBeforeHit);
        float remainingHealth = healthBeforeHit - healthRestored;
        float absorptionOverflow = lethalDamage - healthRestored;

        entity.level().playSound(null, entity, SPSoundEvents.CELESTIAL_GUARDIAN.get(), entity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1f, 1f);
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20, 4));
        if (remainingHealth <= 0f && absorptionOverflow < ABSORPTION_OVERFLOW_CAP) {
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() - absorptionOverflow);
            return true;
        }
        return false;
    }
}

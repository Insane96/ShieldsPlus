package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPSoundEvents;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class CelestialGuardianEnchantment extends Enchantment implements IBlockingEffect, IEnchantmentTooltip {

	public static String LAST_TRIGGER_TAG = ShieldsPlus.RESOURCE_PREFIX + "celestial_guardian_last_trigger";
	public static String SHOULD_APPLY_TAG = ShieldsPlus.RESOURCE_PREFIX + "celestial_guardian";

	public CelestialGuardianEnchantment() {
		super(Rarity.VERY_RARE, SPShieldItem.ENCHANTMENT_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int pEnchantmentLevel) {
		return pEnchantmentLevel * 25;
	}

	public int getMaxCost(int pEnchantmentLevel) {
		return this.getMinCost(pEnchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int lvl, ShieldBlockEvent event) {
        if (lvl <= 0
				|| blockingEntity.getPersistentData().getLong(LAST_TRIGGER_TAG) + 2 * 60 * 20 > blockingEntity.level().getGameTime())
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
		if (!entity.getPersistentData().contains(SHOULD_APPLY_TAG))
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
		entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20, 2));
		if (health <= 0f && amount < 12f) {
			entity.setAbsorptionAmount(entity.getAbsorptionAmount() - amount);
			return true;
		}
		return false;
	}

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_PURPLE);
	}
}

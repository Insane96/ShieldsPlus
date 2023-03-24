package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShieldBashEnchantment extends Enchantment {

	public static final String SHIELD_BASHING = ShieldsPlus.RESOURCE_PREFIX + "shield_bashing";
	public static final String SHIELD_BASHING_LEVEL = ShieldsPlus.RESOURCE_PREFIX + "shield_bashing_level";
	public static final String CHARGE_UP_TIMER = ShieldsPlus.RESOURCE_PREFIX + "charge_up_timer";

	public ShieldBashEnchantment() {
		super(Rarity.VERY_RARE, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int level) {
		return 25;
	}

	public int getMaxCost(int level) {
		return this.getMinCost(level) + 22;
	}

	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return !(enchantment instanceof ShieldReflectionEnchantment) && !(enchantment instanceof ShieldRecoilEnchantment) && super.checkCompatibility(enchantment);
	}

	public static double getForce(byte level) {
		return 3d + (0.5d * (level - 1));
	}

	public static void onTick(Player player) {
		byte shieldBash = (byte) player.getUseItem().getEnchantmentLevel(SPEnchantments.SHIELD_BASH.get());

		cooldownTickChargeUpTimer(player);

		if (player.isBlocking() && getBashingTimer(player) <= 0 && shieldBash > 0) {
			if (getChargeUpTimer(player) < 30) {
				incrementChargeUpTimer(player);
				if (getChargeUpTimer(player) >= 30)
					player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1.65f);
			}
			else if (player.isCrouching() && player.isOnGround()) {
				setBashingTimer(player, (byte) 12);
				setBashingLevel(player, shieldBash);
				float x = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
				float z = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
				player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1.3f);
				player.setDeltaMovement(player.getDeltaMovement().add(x * getForce(shieldBash), 0.45d, z * getForce(shieldBash)));
				for (int i = 0; i < 50; i++) {
					player.level.addParticle(ParticleTypes.CLOUD, player.getX() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), player.getY() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d) + 0.9d, player.getZ() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), 0.1, 0, 0.1);
				}
				putChargeUpTimerOnCooldown(player);
			}
		}
		//If not blocking reset charge up cooldown
		else if (getChargeUpTimer(player) > 0) {
			setChargeUpTimer(player, (byte) 0);
		}

		if (getBashingTimer(player) > 0) {
			setBashingTimer(player, (byte) (getBashingTimer(player) - 1));
			if (player.isBlocking()) {
				damageAndKnockback(player, getBashingLevel(player));
			}
			if (getBashingTimer(player) <= 0)
				setBashingLevel(player, (byte) 0);
		}
	}

	public static byte getChargeUpTimer(Player player) {
		return player.getPersistentData().getByte(CHARGE_UP_TIMER);
	}

	public static void setChargeUpTimer(Player player, byte timer) {
		player.getPersistentData().putByte(CHARGE_UP_TIMER, timer);
	}

	public static void cooldownTickChargeUpTimer(Player player) {
		if (getChargeUpTimer(player) < 0)
			incrementChargeUpTimer(player);
	}

	public static void incrementChargeUpTimer(Player player) {
		setChargeUpTimer(player, (byte) (getChargeUpTimer(player) + 1));
	}

	public static void putChargeUpTimerOnCooldown(Player player) {
		setChargeUpTimer(player, (byte) -30);
	}

	public static byte getBashingTimer(Player player) {
		return player.getPersistentData().getByte(SHIELD_BASHING);
	}

	public static void setBashingTimer(Player player, byte timer) {
		player.getPersistentData().putByte(SHIELD_BASHING, timer);
	}

	public static byte getBashingLevel(Player player) {
		return player.getPersistentData().getByte(SHIELD_BASHING_LEVEL);
	}

	public static void setBashingLevel(Player player, byte level) {
		player.getPersistentData().putByte(SHIELD_BASHING_LEVEL, level);
	}

	public static void damageAndKnockback(Player player, int level) {
		AABB aabb = player.getBoundingBox().inflate(0.6d, 0.2d, 0.6d);
		List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> entity != player);
		for (LivingEntity entity : entities) {
			if (entity.isDeadOrDying() || entity.invulnerableTime > 10)
				continue;
			entity.knockback(1d + (0.4d * level), player.getX() - entity.getX(), player.getZ() - entity.getZ());
			//TODO Send knockback packet to player
			if (entity.hurt(DamageSource.playerAttack(player), 3 + (3 * level))) {
				ShieldAblazeEnchantment.apply(player, entity);
				player.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.5f);
			}
		}
	}
}

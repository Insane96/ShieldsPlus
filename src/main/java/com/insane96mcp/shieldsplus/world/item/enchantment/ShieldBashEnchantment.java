package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import com.insane96mcp.shieldsplus.setup.Strings;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
		return 1;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return !(enchantment instanceof ShieldReflectionEnchantment) && !(enchantment instanceof ShieldRecoilEnchantment) && super.checkCompatibility(enchantment);
	}

	public static void onTick(Player player) {
		CompoundTag tag = player.getPersistentData();
		int shieldBash = player.getUseItem().getEnchantmentLevel(SPEnchantments.SHIELD_BASH.get());

		if (tag.getByte(Strings.Tags.BASH_TIMER) < 0){
			tag.putByte(Strings.Tags.BASH_TIMER, (byte) (tag.getByte(Strings.Tags.BASH_TIMER) + 1));
		}

		if (player.isBlocking() && tag.getByte(Strings.Tags.SHIELD_BASHING) <= 0 && shieldBash > 0) {
			if (tag.getByte(Strings.Tags.BASH_TIMER) < 30) {
				tag.putByte(Strings.Tags.BASH_TIMER, (byte) (tag.getByte(Strings.Tags.BASH_TIMER) + 1));
				if (tag.getByte(Strings.Tags.BASH_TIMER) >= 30)
					player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1.65f);
			}
			else if (player.isCrouching() && player.isOnGround()) {
				tag.putByte(Strings.Tags.SHIELD_BASHING, (byte) 12);
				float f = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
				float f1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
				player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1.3f);
				player.setDeltaMovement(player.getDeltaMovement().add(f * 3d, 0.45d, f1 * 3d));
				for (int i = 0; i < 50; i++) {
					player.level.addParticle(ParticleTypes.CLOUD, player.getX() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), player.getY() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d) + 0.9d, player.getZ() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), 0.1, 0, 0.1);
				}
				tag.putByte(Strings.Tags.BASH_TIMER, (byte) -30);
			}
		}
		else if (tag.getByte(Strings.Tags.BASH_TIMER) > 0) {
			tag.putByte(Strings.Tags.BASH_TIMER, (byte) 0);
		}

		if (tag.getByte(Strings.Tags.SHIELD_BASHING) > 0) {
			tag.putByte(Strings.Tags.SHIELD_BASHING, (byte) (tag.getByte(Strings.Tags.SHIELD_BASHING) - 1));
			if (player.isBlocking()) {
				damageAndKnockback(player);
			}
		}
	}

	public static void damageAndKnockback(Player player) {
		AABB aabb = player.getBoundingBox().inflate(0.6d, 0.2d, 0.6d);
		List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> entity != player);
		for (LivingEntity entity : entities) {
			if (entity.isDeadOrDying() || entity.invulnerableTime > 10)
				continue;
			entity.knockback(1.5d, player.getX() - entity.getX(), player.getZ() - entity.getZ());
			if (entity.hurt(DamageSource.playerAttack(player), 6)) {
				player.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.5f);
				ShieldAblazeEnchantment.apply(player, entity);
			}
		}
	}
}

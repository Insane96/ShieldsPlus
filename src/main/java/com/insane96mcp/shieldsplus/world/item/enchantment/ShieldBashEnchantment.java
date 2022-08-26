package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.insanelib.util.MCUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ShieldBashEnchantment extends Enchantment {

	public ShieldBashEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
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
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ShieldItem;
	}

	@Override
	public boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return !(enchantment instanceof ShieldReflectionEnchantment) && !(enchantment instanceof ShieldRecoilEnchantment) && super.checkCompatibility(enchantment);
	}

	public static void onTick(Player player) {
		int shieldBash = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.SHIELD_BASH.get(), player.getUseItem());
		if (shieldBash <= 0)
			return;

		CompoundTag tag = player.getPersistentData();
		if (player.isBlocking() && tag.getByte("shield_bashing") <= 0) {
			if (tag.getByte("bash_timer") < 30) {
				tag.putByte("bash_timer", (byte) (tag.getByte("bash_timer") + 1));
				if (tag.getByte("bash_timer") >= 30)
					player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 1.65f);
			}
			else if (player.isCrouching() && player.isOnGround()) {
				//player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 10, true, true, false));
				tag.putByte("shield_bashing", (byte) 12);
				MCUtils.applyModifier(player, ForgeMod.STEP_HEIGHT_ADDITION.get(), UUID.fromString("259f26e7-6914-4856-a01f-8b2295e6d244"), "Shield Bash step height", 0.6d, AttributeModifier.Operation.ADDITION, false);
				float f = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
				float f1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
				player.playSound(SoundEvents.SHIELD_BLOCK, 1f, 2.0f);
				player.setDeltaMovement(player.getDeltaMovement().add(f * 2.75d, 0.4d, f1 * 2.75d));
				for (int i = 0; i < 50; i++) {
					player.level.addParticle(ParticleTypes.CLOUD, player.getX() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), player.getY() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d) + 0.9d, player.getZ() + Mth.nextDouble(player.getRandom(), -0.5d, 0.5d), 0.1, 0, 0.1);
				}
				tag.putByte("bash_timer", (byte) -30);
			}
		}
		else if (tag.getByte("bash_timer") > 30){
			tag.putByte("bash_timer", (byte) 0);
		}
		else {
			tag.putByte("bash_timer", (byte) (tag.getByte("bash_timer") + 1));
		}

		if (tag.getByte("shield_bashing") > 0) {
			tag.putByte("shield_bashing", (byte) (tag.getByte("shield_bashing") - 1));
			if (tag.getByte("shield_bashing") == 0) {
				if (player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()) != null)
					//noinspection ConstantConditions
					player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(UUID.fromString("259f26e7-6914-4856-a01f-8b2295e6d244"));
			}
			if (player.isBlocking()) {
				damageAndKnockback(player);
			}
		}
	}

	public static void damageAndKnockback(Player player) {
		AABB aabb = player.getBoundingBox().inflate(1.25d, 0.5d, 1.25d);
		List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> entity != player);
		for (LivingEntity entity : entities) {
			if (entity.isDeadOrDying() || entity.invulnerableTime > 10)
				continue;
			entity.knockback(1.5d, player.getX() - entity.getX(), player.getZ() - entity.getZ());
			if (entity.hurt(DamageSource.playerAttack(player), 5)) {
				player.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 0.5f);
				ShieldAblazeEnchantment.apply(player, entity);
			}
		}
	}
}

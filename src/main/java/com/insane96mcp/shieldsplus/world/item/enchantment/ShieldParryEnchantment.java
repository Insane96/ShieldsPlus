package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.insanelib.util.LogHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ShieldParryEnchantment extends Enchantment {

	public static final MobEffectInstance SLOWNESS = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 4);
	public static final MobEffectInstance WEAKNESS = new MobEffectInstance(MobEffects.WEAKNESS, 50, 2);

	public ShieldParryEnchantment() {
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
		return !(enchantment instanceof ShieldBashEnchantment) && super.checkCompatibility(enchantment);
	}

	public static void onBlocked(LivingEntity blockingEntity, DamageSource source) {
		ItemStack shield = blockingEntity.getUseItem();
		int parry = shield.getEnchantmentLevel(SPEnchantments.PARRY.get());
		LogHelper.info("ticksUsingItem %s".formatted(blockingEntity.getTicksUsingItem()));
		if (parry > 0 && blockingEntity.getTicksUsingItem() <= 5)
		{
			if (source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()) {
				sourceEntity.knockback(1d, blockingEntity.getX() - sourceEntity.getX(), blockingEntity.getZ() - sourceEntity.getZ());
				sourceEntity.addEffect(new MobEffectInstance(SLOWNESS));
				sourceEntity.addEffect(new MobEffectInstance(WEAKNESS));
			}
		}
	}
}

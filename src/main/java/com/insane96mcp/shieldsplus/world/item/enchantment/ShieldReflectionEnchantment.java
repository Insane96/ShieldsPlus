package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ShieldReflectionEnchantment extends Enchantment {

	public static final float REFLECTED_DAMAGE = 0.15f;
	public static final float CAPPED_REFLECTED_DAMAGE = 2f;
	public static final float BLOCKED_DAMAGE_REDUCTION = 0.1f;

	public ShieldReflectionEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 9 + (p_44598_ - 1) * 14;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 14;
	}

	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.getItem() instanceof ShieldItem;
	}

	public static float getReflectedDamage(int level) {
		return level * REFLECTED_DAMAGE;
	}

	public static float getReflectedDamage(ItemStack stack) {
		return getReflectedDamage(EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), stack));
	}

	public static float getCappedReflectedDamage(int level) {
		return level * CAPPED_REFLECTED_DAMAGE;
	}

	public static float getCappedReflectedDamage(ItemStack stack) {
		return getReflectedDamage(EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), stack));
	}

	public static float getBlockedDamageReduction(int level) {
		return level * BLOCKED_DAMAGE_REDUCTION;
	}

	public static float getBlockedDamageReduction(ItemStack stack) {
		return getBlockedDamageReduction(EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), stack));
	}
}

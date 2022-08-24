package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ShieldReinforcedEnchantment extends Enchantment {

	public static final double DAMAGE_BLOCKED = 0.5d;

	public ShieldReinforcedEnchantment() {
		super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 8 + (p_44598_ - 1) * 10;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 15;
	}

	public int getMaxLevel() {
		return 4;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.getItem() instanceof ShieldItem;
	}

	public static double getDamageBlocked(int level) {
		return DAMAGE_BLOCKED * level;
	}

	public static double getDamageBlocked(ItemStack itemStack) {
		return getDamageBlocked(EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.REINFORCED.get(), itemStack));
	}
}

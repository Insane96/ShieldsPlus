package com.insane96mcp.shieldsplus.world.item.enchantment;

import com.insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.UUID;

public class ShieldLightweightEnchantment extends Enchantment {

	public static final double BONUS_SPEED = 1d;
	public static final UUID BONUS_SPEED_UUID = UUID.fromString("bf9ce34a-a825-4b22-a050-f6f752879332");

	public ShieldLightweightEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 22;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 22;
	}

	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.getItem() instanceof ShieldItem;
	}

	public static void onTick(Player player) {
		AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attribute == null)
			return;

		if (player.isBlocking()) {
			int lightweight = EnchantmentHelper.getItemEnchantmentLevel(SPEnchantments.LIGHTWEIGHT.get(), player.getUseItem());
			if (lightweight > 0) {

				if (attribute.getModifier(ShieldLightweightEnchantment.BONUS_SPEED_UUID) == null) {
					attribute.addTransientModifier(new AttributeModifier(BONUS_SPEED_UUID, "Lightweight bonus speed", BONUS_SPEED * lightweight, AttributeModifier.Operation.MULTIPLY_BASE));
				}
			}
		}
		else if (attribute.getModifier(ShieldLightweightEnchantment.BONUS_SPEED_UUID) != null) {
			attribute.removeModifier(ShieldLightweightEnchantment.BONUS_SPEED_UUID);
		}
	}
}

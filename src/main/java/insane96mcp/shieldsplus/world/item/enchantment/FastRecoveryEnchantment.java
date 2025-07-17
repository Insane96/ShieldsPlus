package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class FastRecoveryEnchantment extends Enchantment {
	public FastRecoveryEnchantment() {
		super(Rarity.RARE, SPShieldItem.ENCHANTMENT_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 22;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 22;
	}

	public static float getCooldownReduction(int lvl) {
		return BaseFeature.enchantments$fastRecoveryCooldownReduction.floatValue();
	}
}

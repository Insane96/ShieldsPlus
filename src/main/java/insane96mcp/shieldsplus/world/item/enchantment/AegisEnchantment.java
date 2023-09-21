package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class AegisEnchantment extends Enchantment {

	public static final float RESISTANCE = 0.1f;

	public AegisEnchantment() {
		super(Rarity.COMMON, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 4 + (p_44598_ - 1) * 8;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 15;
	}

	public int getMaxLevel() {
		return 5;
	}

	@Override
	public boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return !(enchantment instanceof ReflectionEnchantment) && !(enchantment instanceof ReinforcedEnchantment) && super.checkCompatibility(enchantment);
	}

	public static float getResistance(int level) {
		return RESISTANCE * level;
	}

	public static float getResistance(ItemStack itemStack) {
		return getResistance(itemStack.getEnchantmentLevel(SPEnchantments.AEGIS.get()));
	}
}

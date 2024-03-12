package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.InsaneLib;
import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;

public class AegisEnchantment extends Enchantment implements IEnchantmentTooltip {
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

	@Override
	public boolean canEnchant(ItemStack pStack) {
		return BaseFeature.shieldBlockFixedDamageAmount && super.canEnchant(pStack);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return BaseFeature.shieldBlockFixedDamageAmount && super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean isTradeable() {
		return BaseFeature.shieldBlockFixedDamageAmount && super.isTradeable();
	}

	@Override
	public boolean isDiscoverable() {
		return BaseFeature.shieldBlockFixedDamageAmount && super.isDiscoverable();
	}

	@Override
	public boolean isAllowedOnBooks() {
		return BaseFeature.shieldBlockFixedDamageAmount && super.isAllowedOnBooks();
	}

	public static float getResistance(int level) {
		return BaseFeature.enchantmentsAegisPercentageDamageReduction.floatValue() * level;
	}

	public static float getResistance(ItemStack itemStack) {
		return getResistance(itemStack.getEnchantmentLevel(SPEnchantments.AEGIS.get()));
	}

	public static void reduceDamage(LivingDamageEvent event) {
		int lvl = event.getEntity().getUseItem().getEnchantmentLevel(SPEnchantments.AEGIS.get());
		if (lvl <= 0)
			return;

		event.setAmount(event.getAmount() * (1f - AegisEnchantment.getResistance(lvl)));
	}

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip", InsaneLib.ONE_DECIMAL_FORMATTER.format(getResistance(lvl) * 100f)).withStyle(ChatFormatting.DARK_PURPLE);
	}
}

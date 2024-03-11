package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.InsaneLib;
import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ReflectionEnchantment extends Enchantment implements IBlockingEffect, IEnchantmentTooltip {

	public static final float REFLECTED_DAMAGE = 0.125f;
	public static final float CAPPED_REFLECTED_DAMAGE = 2f;

	public ReflectionEnchantment() {
		super(Rarity.RARE, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 8 + (p_44598_ - 1) * 10;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 14;
	}

	public int getMaxLevel() {
		return 4;
	}

	@Override
	public boolean checkCompatibility(@NotNull Enchantment enchantment) {
		return !(enchantment instanceof ReinforcedEnchantment) && super.checkCompatibility(enchantment);
	}

	public static float getReflectedDamage(int level) {
		return level * REFLECTED_DAMAGE;
	}

	public static float getCappedReflectedDamage(int level) {
		return level * CAPPED_REFLECTED_DAMAGE;
	}

	public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int lvl) {
		if ((!(source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()))
				|| lvl <= 0)
			return;

		sourceEntity.hurt(blockingEntity.damageSources().thorns(blockingEntity), Math.min(ReflectionEnchantment.getReflectedDamage(lvl) * amount, ReflectionEnchantment.getCappedReflectedDamage(lvl)));
	}

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip", InsaneLib.ONE_DECIMAL_FORMATTER.format(getReflectedDamage(lvl) * 100f), InsaneLib.ONE_DECIMAL_FORMATTER.format(getCappedReflectedDamage(lvl) * 100f)).withStyle(ChatFormatting.DARK_PURPLE);
	}
}

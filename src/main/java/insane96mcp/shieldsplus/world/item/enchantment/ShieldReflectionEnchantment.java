package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ShieldReflectionEnchantment extends Enchantment {

	public static final float REFLECTED_DAMAGE = 0.125f;
	public static final float CAPPED_REFLECTED_DAMAGE = 2f;

	public ShieldReflectionEnchantment() {
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
		return !(enchantment instanceof ShieldReinforcedEnchantment) && super.checkCompatibility(enchantment);
	}

	public static float getReflectedDamage(int level) {
		return level * REFLECTED_DAMAGE;
	}

	public static float getCappedReflectedDamage(int level) {
		return level * CAPPED_REFLECTED_DAMAGE;
	}

	public static void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount) {
		if (!(source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()))
			return;
		ItemStack shield = blockingEntity.getUseItem();
		int reflection = shield.getEnchantmentLevel(SPEnchantments.REFLECTION.get());
		if (reflection > 0)
			sourceEntity.hurt(DamageSource.thorns(blockingEntity), Math.min(ShieldReflectionEnchantment.getReflectedDamage(reflection) * amount, ShieldReflectionEnchantment.getCappedReflectedDamage(reflection)));
	}
}

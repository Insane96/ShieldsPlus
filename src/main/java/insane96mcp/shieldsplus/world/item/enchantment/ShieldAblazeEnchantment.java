package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ShieldAblazeEnchantment extends Enchantment {

	public static final int SECONDS_ON_FIRE = 3;

	public ShieldAblazeEnchantment() {
		super(Rarity.UNCOMMON, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 12 + (p_44598_ - 1) * 20;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 25;
	}

	public int getMaxLevel() {
		return 2;
	}

	public static void onBlocked(LivingEntity blockingEntity, DamageSource source) {
		if (source.getDirectEntity() != null)
			apply(blockingEntity, source.getDirectEntity());
	}

	public static void apply(LivingEntity attacker, Entity other) {
		ItemStack shield = attacker.getUseItem();
		int ablaze = shield.getEnchantmentLevel(SPEnchantments.ABLAZE.get());
		if (ablaze > 0)
			other.setSecondsOnFire(ablaze * ShieldAblazeEnchantment.SECONDS_ON_FIRE);
	}
}
package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ShieldRecoilEnchantment extends Enchantment {

	public static final double KNOCKBACK = 0.65d;
	public static final double PROJECTILE_KNOCKBACK = 5d;

	public ShieldRecoilEnchantment() {
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
		ItemStack shield = blockingEntity.getUseItem();
		int recoil = shield.getEnchantmentLevel(SPEnchantments.RECOIL.get());
		if (recoil > 0)
		{
			if (source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity()) {
				sourceEntity.knockback(recoil * ShieldRecoilEnchantment.KNOCKBACK, blockingEntity.getX() - sourceEntity.getX(), blockingEntity.getZ() - sourceEntity.getZ());
			}
			else if (source.getDirectEntity() instanceof Projectile projectile) {
				projectile.setDeltaMovement(projectile.getDeltaMovement().scale(recoil * ShieldRecoilEnchantment.PROJECTILE_KNOCKBACK));
			}
		}
	}
}

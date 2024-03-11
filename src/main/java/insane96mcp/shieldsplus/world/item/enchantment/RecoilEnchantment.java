package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class RecoilEnchantment extends Enchantment implements IBlockingEffect, IEnchantmentTooltip {

	public static final double KNOCKBACK = 0.6d;
	public static final double PROJECTILE_KNOCKBACK = 5d;

	public RecoilEnchantment() {
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

	public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int lvl) {
        if (lvl <= 0)
            return;

        if (source.getEntity() instanceof LivingEntity sourceEntity && source.getEntity() == source.getDirectEntity())
            sourceEntity.knockback(lvl * RecoilEnchantment.KNOCKBACK, blockingEntity.getX() - sourceEntity.getX(), blockingEntity.getZ() - sourceEntity.getZ());
        else if (source.getDirectEntity() instanceof Projectile projectile)
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(lvl * RecoilEnchantment.PROJECTILE_KNOCKBACK));
    }

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_PURPLE);
	}
}

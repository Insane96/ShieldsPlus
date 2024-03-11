package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.insanelib.world.scheduled.ScheduledTasks;
import insane96mcp.insanelib.world.scheduled.ScheduledTickTask;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class AblazeEnchantment extends Enchantment implements IBlockingEffect, IEnchantmentTooltip {

	public static final int SECONDS_ON_FIRE = 2;

	public AblazeEnchantment() {
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
		if (source.getDirectEntity() != null)
			apply(blockingEntity, source.getDirectEntity());
	}

	public static void apply(LivingEntity attacker, Entity other) {
		ItemStack shield = attacker.getUseItem();
		int ablaze = shield.getEnchantmentLevel(SPEnchantments.ABLAZE.get());
		if (ablaze > 0)
			ScheduledTasks.schedule(new ScheduledTickTask(1) {
				@Override
				public void run() {
					other.setSecondsOnFire(ablaze * AblazeEnchantment.SECONDS_ON_FIRE);
					if (other instanceof LivingEntity livingEntity) {
						livingEntity.setLastHurtByMob(attacker);
						if (livingEntity instanceof Player player)
							livingEntity.setLastHurtByPlayer(player);
					}
				}
			});
	}

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip", SECONDS_ON_FIRE * lvl).withStyle(ChatFormatting.DARK_PURPLE);
	}
}

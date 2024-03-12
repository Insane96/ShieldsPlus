package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.world.enchantments.IEnchantmentTooltip;
import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class PerfectParryEnchantment extends Enchantment implements IBlockingEffect, IEnchantmentTooltip {

	public PerfectParryEnchantment() {
		super(Rarity.VERY_RARE, SPShieldItem.SHIELD, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
	}

	public int getMinCost(int p_44598_) {
		return 12 + (p_44598_ - 1) * 20;
	}

	public int getMaxCost(int p_44600_) {
		return this.getMinCost(p_44600_) + 25;
	}

	public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int lvl, ShieldBlockEvent event) {
        if (lvl <= 0)
            return;

        int ticksSinceBlocking = blockingEntity.getUseItem().getUseDuration() - blockingEntity.getUseItemRemainingTicks();
		if (ticksSinceBlocking <= BaseFeature.enchantmentsPerfectParryTickWindow) {
			//event.getEntity().level().broadcastEntityEvent(event.getEntity(), EntityEvent.ATTACK_BLOCKED);
			event.setBlockedDamage(1024f);
		}
    }

	@Override
	public Component getTooltip(ItemStack itemStack, int lvl) {
		return Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_PURPLE);
	}
}

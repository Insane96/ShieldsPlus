package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.BaseFeature;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class PerfectParryEnchantment extends Enchantment implements IBlockingEffect {

	public PerfectParryEnchantment() {
		super(Rarity.VERY_RARE, SPShieldItem.ENCHANTMENT_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
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
		if (ticksSinceBlocking <= BaseFeature.enchantments$perfectParryTickWindow) {
			//event.getEntity().level().broadcastEntityEvent(event.getEntity(), EntityEvent.ATTACK_BLOCKED);
			event.setBlockedDamage(1024f);
			if (source.getDirectEntity() instanceof LivingEntity entity) {
				entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 99, true, true));
				entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50, 99, true, true));
				if (entity instanceof Mob mob)
					mob.setTarget(null);
			}
		}
    }
}

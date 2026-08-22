package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.world.scheduled.ScheduledTasks;
import insane96mcp.insanelib.world.scheduled.ScheduledTickTask;
import insane96mcp.shieldsplus.module.SPFeature;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class AblazeEnchantmentEffect implements IBlockingEnchantmentEffect {
    @Override
    public void onBlocked(LivingEntity blockingEntity, DamageSource source, float amount, int level, LivingShieldBlockEvent event) {
        Entity target = source.getDirectEntity();
        if (target == null)
            return;

        ScheduledTasks.schedule(new ScheduledTickTask(1) {
            @Override
            public void run() {
                target.igniteForSeconds(level * SPFeature.enchantments$ablazeTimeOnFire);
                if (target instanceof LivingEntity livingTarget) {
                    livingTarget.setLastHurtByMob(blockingEntity);
                    if (livingTarget instanceof Player player)
                        livingTarget.setLastHurtByPlayer(player);
                }
            }
        });
    }
}

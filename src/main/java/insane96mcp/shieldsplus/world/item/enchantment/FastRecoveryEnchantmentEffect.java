package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Not an {@link IBlockingEnchantmentEffect}: it shortens the shield-disable cooldown duration itself,
 * applied directly from {@link insane96mcp.shieldsplus.mixin.PlayerMixin#shieldsPlus$disableShield}.
 */
public class FastRecoveryEnchantmentEffect {
    public static int reduceCooldown(ItemStack itemStack, int ticks, RegistryLookup<Enchantment> enchantmentLookup) {
        int level = enchantmentLookup.get(SPEnchantments.FAST_RECOVERY).map(itemStack::getEnchantmentLevel).orElse(0);
        if (level <= 0)
            return ticks;

        return (int) (ticks * (1 - SPFeature.enchantments$fastRecoveryCooldownReduction));
    }
}

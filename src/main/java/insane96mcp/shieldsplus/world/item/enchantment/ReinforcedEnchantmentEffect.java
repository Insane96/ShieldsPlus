package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.RuneCompat;
import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Reinforced isn't a {@link IBlockingEnchantmentEffect} since it doesn't react to a block happening, it modifies
 * how much damage a block absorbs in the first place. Applied directly from
 * {@link SPFeature#onShieldBlock} where the base blocked damage is computed.
 */
public class ReinforcedEnchantmentEffect {
    public static float getDamageBlocked(ItemStack itemStack, RegistryLookup<Enchantment> enchantmentLookup) {
        int level = enchantmentLookup.get(SPEnchantments.REINFORCED).map(itemStack::getEnchantmentLevel).orElse(0);
        level += RuneCompat.getRuneLevel(itemStack, SPEnchantments.REINFORCED);
        return SPFeature.enchantments$reinforcedBlockedDamageBonus.floatValue() * level;
    }

    public static float increaseDamageBlocked(ItemStack itemStack, float damageBlocked, RegistryLookup<Enchantment> enchantmentLookup) {
        return damageBlocked + (damageBlocked * getDamageBlocked(itemStack, enchantmentLookup));
    }
}

package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.shieldsplus.module.RuneCompat;
import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/**
 * Aegis isn't a {@link IBlockingEnchantmentEffect} either: it reduces damage taken while the shield is
 * raised regardless of whether that hit was actually blocked, so it hooks {@link LivingDamageEvent.Pre}
 * instead of the shield block event.
 */
public class AegisEnchantmentEffect {
    public static float getResistance(ItemStack itemStack, RegistryLookup<Enchantment> enchantmentLookup) {
        int level = enchantmentLookup.get(SPEnchantments.AEGIS).map(itemStack::getEnchantmentLevel).orElse(0);
        level += RuneCompat.getRuneLevel(itemStack, SPEnchantments.AEGIS);
        return SPFeature.enchantments$aegisPercentageDamageReduction.floatValue() * level;
    }

    public static void reduceDamage(LivingDamageEvent.Pre event, RegistryLookup<Enchantment> enchantmentLookup) {
        float resistance = getResistance(event.getEntity().getUseItem(), enchantmentLookup);
        if (resistance <= 0)
            return;

        event.setNewDamage(event.getNewDamage() * (1f - resistance));
    }
}

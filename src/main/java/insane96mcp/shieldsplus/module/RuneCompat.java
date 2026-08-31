package insane96mcp.shieldsplus.module;

import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;

/**
 * Thin, always-safe bridge to the optional Rune Enchanting integration ({@link SPRunes}). This class never
 * references any Rune Enchanting class directly, so it loads fine even when the mod isn't installed -
 * {@link #getRuneLevel} only touches {@link SPRunes} once {@link #isLoaded()} confirms it's safe to, keeping
 * {@link SPRunes} (and its hard references to Rune Enchanting's classes) from ever being loaded otherwise.
 */
public class RuneCompat {
    private static final String MOD_ID = "runeenchanting";
    @Nullable
    private static Boolean loaded;

    public static boolean isLoaded() {
        if (loaded == null)
            loaded = ModList.get().isLoaded(MOD_ID);
        return loaded;
    }

    /**
     * Returns the extra enchantment level granted by a Shields+ rune equivalent to {@code enchantment} on
     * {@code stack}, or 0 if there's none. Meant to be added to the item's real enchantment level.
     */
    public static int getRuneLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        if (!SPFeature.enchantments$runesEnabled || !isLoaded())
            return 0;

        int levelEquivalent = levelEquivalentFor(enchantment);
        if (levelEquivalent <= 0)
            return 0;

        return SPRunes.getRuneLevel(stack, enchantment, levelEquivalent);
    }

    private static int levelEquivalentFor(ResourceKey<Enchantment> enchantment) {
        if (enchantment.equals(SPEnchantments.RECOIL))
            return SPFeature.enchantments$recoilRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.REINFORCED))
            return SPFeature.enchantments$reinforcedRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.AEGIS))
            return SPFeature.enchantments$aegisRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.ABLAZE))
            return SPFeature.enchantments$ablazeRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.LIGHTWEIGHT))
            return SPFeature.enchantments$lightweightRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.FAST_RECOVERY))
            return SPFeature.enchantments$fastRecoveryRuneLevelEquivalent;
        if (enchantment.equals(SPEnchantments.CELESTIAL_GUARDIAN))
            return SPFeature.enchantments$celestialGuardianRuneLevelEquivalent;
        return 0;
    }
}

package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.world.item.enchantment.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * Enchantments are data-driven (data/shieldsplus/enchantment/*.json), these are just typed keys pointing
 * to that data. Custom gameplay behaviour that vanilla's data-driven effects can't express is dispatched
 * through {@link #BLOCKING_EFFECTS} from {@link insane96mcp.shieldsplus.module.SPFeature#onShieldBlock}.
 */
public class SPEnchantments {
    public static final ResourceKey<Enchantment> RECOIL = key("recoil");
    public static final ResourceKey<Enchantment> REFLECTION = key("reflection");
    public static final ResourceKey<Enchantment> REINFORCED = key("reinforced");
    public static final ResourceKey<Enchantment> AEGIS = key("aegis");
    public static final ResourceKey<Enchantment> ABLAZE = key("ablaze");
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = key("lightweight");
    public static final ResourceKey<Enchantment> FAST_RECOVERY = key("fast_recovery");
    public static final ResourceKey<Enchantment> CELESTIAL_GUARDIAN = key("celestial_guardian");

    public static final Map<ResourceKey<Enchantment>, IBlockingEnchantmentEffect> BLOCKING_EFFECTS = Map.of(
            RECOIL, new RecoilEnchantmentEffect(),
            REFLECTION, new ReflectionEnchantmentEffect(),
            ABLAZE, new AblazeEnchantmentEffect(),
            CELESTIAL_GUARDIAN, new CelestialGuardianEnchantmentEffect()
    );

    private static ResourceKey<Enchantment> key(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, ShieldsPlus.id(id));
    }
}

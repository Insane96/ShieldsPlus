package insane96mcp.shieldsplus.module;

import insane96mcp.insanelib.InsaneLib;
import insane96mcp.runeenchanting.RuneHelper;
import insane96mcp.runeenchanting.runes.Rune;
import insane96mcp.runeenchanting.setup.RERunes;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers a Rune Enchanting equivalent for each of Shields+'s own shield enchantments (recoil, reinforced,
 * aegis, ablaze, lightweight, fast recovery, celestial guardian), so the effect can also be obtained from a
 * rune instead of only from the real enchantment. Only ever constructed if Rune Enchanting is installed (see
 * {@link RuneCompat} and {@link insane96mcp.shieldsplus.ShieldsPlus}), so this class - and therefore its
 * references to Rune Enchanting's classes - is never loaded otherwise.
 * <p>
 * These runes carry no gameplay logic of their own: {@link RuneCompat#getRuneLevel} is queried directly from
 * the same places that already read the real enchantment level ({@code SPFeature#onShieldBlock} and the
 * various {@code *EnchantmentEffect} helpers), and the returned rune-equivalent level is simply added on top.
 */
public class SPRunes {
    private static final Map<ResourceKey<Enchantment>, Rune> RUNES = new LinkedHashMap<>();

    public SPRunes(IEventBus modEventBus) {
        modEventBus.addListener(this::onRegister);
    }

    private void onRegister(RegisterEvent event) {
        event.register(RERunes.REGISTRY_KEY, helper -> {
            register(helper, SPEnchantments.RECOIL, "recoil", new RecoilRune());
            register(helper, SPEnchantments.REINFORCED, "reinforced", new ReinforcedRune());
            register(helper, SPEnchantments.AEGIS, "aegis", new AegisRune());
            register(helper, SPEnchantments.ABLAZE, "ablaze", new AblazeRune());
            register(helper, SPEnchantments.LIGHTWEIGHT, "lightweight", new LightweightRune());
            register(helper, SPEnchantments.FAST_RECOVERY, "fast_recovery", new FastRecoveryRune());
            register(helper, SPEnchantments.CELESTIAL_GUARDIAN, "celestial_guardian", new CelestialGuardianRune());
        });
    }

    private static void register(RegisterEvent.RegisterHelper<Rune> helper, ResourceKey<Enchantment> enchantment, String id, Rune rune) {
        helper.register(ShieldsPlus.id(id), rune);
        RUNES.put(enchantment, rune);
    }

    /**
     * Returns {@code levelEquivalent} if {@code stack} carries the rune equivalent to {@code enchantment}, 0
     * otherwise. Called only through {@link RuneCompat#getRuneLevel}, which already checked this class is safe
     * to touch.
     */
    public static int getRuneLevel(ItemStack stack, ResourceKey<Enchantment> enchantment, int levelEquivalent) {
        Rune rune = RUNES.get(enchantment);
        if (rune == null)
            return 0;

        Holder<Rune> holder = RERunes.REGISTRY.wrapAsHolder(rune);
        return RuneHelper.hasRune(stack, holder) ? levelEquivalent : 0;
    }

    private abstract static class ShieldRune extends Rune {
        private final String name;
        private final String description;

        private ShieldRune(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        /** Item applicability is data-driven (data/shieldsplus/tags/item/rune_applicable_to/*.json) instead. */
        @Override
        public void addItemsToApplicableTag(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> appender) {}
    }

    public static class RecoilRune extends ShieldRune {
        public RecoilRune() {
            super("Recoil", "Knocks back attackers and deflects projectiles when blocking with a shield");
        }

        @Override
        public @Nullable String getInfo() {
            return "Entity knockback: +%s, Projectile knockback: x%s";
        }

        @Override
        public MutableComponent getInfoComponent() {
            int lvl = SPFeature.enchantments$recoilRuneLevelEquivalent;
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(lvl * SPFeature.enchantments$recoilEntitiesKnockback),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(lvl * SPFeature.enchantments$recoilProjectilesKnockback));
        }
    }

    public static class ReinforcedRune extends ShieldRune {
        public ReinforcedRune() {
            super("Reinforced", "Increases the amount of damage blocked by a shield");
        }

        @Override
        public @Nullable String getInfo() {
            return "Damage blocked: +%s%%";
        }

        @Override
        public MutableComponent getInfoComponent() {
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(SPFeature.enchantments$reinforcedRuneLevelEquivalent * SPFeature.enchantments$reinforcedBlockedDamageBonus * 100));
        }
    }

    public static class AegisRune extends ShieldRune {
        public AegisRune() {
            super("Aegis", "Reduces damage taken while blocking with a shield");
        }

        @Override
        public @Nullable String getInfo() {
            return "Damage reduction: %s%%";
        }

        @Override
        public MutableComponent getInfoComponent() {
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(SPFeature.enchantments$aegisRuneLevelEquivalent * SPFeature.enchantments$aegisPercentageDamageReduction * 100));
        }
    }

    public static class AblazeRune extends ShieldRune {
        public AblazeRune() {
            super("Ablaze", "Sets attackers on fire when blocking with a shield");
        }

        @Override
        public @Nullable String getInfo() {
            return "Time on fire: %ss";
        }

        @Override
        public MutableComponent getInfoComponent() {
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(SPFeature.enchantments$ablazeRuneLevelEquivalent * SPFeature.enchantments$ablazeTimeOnFire));
        }
    }

    public static class LightweightRune extends ShieldRune {
        public LightweightRune() {
            super("Lightweight", "Increases movement speed while blocking with a shield");
        }

        @Override
        public @Nullable String getInfo() {
            return "Movement speed: +%s%%";
        }

        @Override
        public MutableComponent getInfoComponent() {
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(SPFeature.enchantments$lightweightRuneLevelEquivalent * SPFeature.enchantments$lightweightBonusSpeed * 100));
        }
    }

    public static class FastRecoveryRune extends ShieldRune {
        public FastRecoveryRune() {
            super("Fast Recovery", "Reduces the shield's cooldown after it's disabled");
        }

        @Override
        public @Nullable String getInfo() {
            return "Cooldown reduction: %s%%";
        }

        @Override
        public MutableComponent getInfoComponent() {
            return Component.translatable(getInfoTranslationKey(),
                    InsaneLib.ONE_DECIMAL_FORMATTER.format(SPFeature.enchantments$fastRecoveryCooldownReduction * 100));
        }
    }

    public static class CelestialGuardianRune extends ShieldRune {
        public CelestialGuardianRune() {
            super("Celestial Guardian", "Blocking an attack can save you from a lethal blow soon after");
        }
    }
}

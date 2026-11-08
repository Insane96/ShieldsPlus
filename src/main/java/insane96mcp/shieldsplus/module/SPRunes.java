package insane96mcp.shieldsplus.module;

import insane96mcp.insanelib.InsaneLib;
import insane96mcp.runeenchanting.RuneFeature;
import insane96mcp.runeenchanting.RuneHelper;
import insane96mcp.runeenchanting.runes.Rune;
import insane96mcp.runeenchanting.setup.RERunes;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers a Rune Enchanting equivalent for each of Shields+'s own shield enchantments (recoil, reinforced,
 * aegis, lightweight, fast recovery, celestial guardian), so the effect can also be obtained from a rune
 * instead of only from the real enchantment. Ablaze reuses Rune Enchanting's own Flame rune instead of
 * registering a new one. Only ever constructed if Rune Enchanting is installed (see
 * {@link RuneCompat} and {@link insane96mcp.shieldsplus.ShieldsPlus}), so this class - and therefore its
 * references to Rune Enchanting's classes - is never loaded otherwise.
 */
public class SPRunes {
    private static final Map<ResourceKey<Enchantment>, Rune> RUNES = new LinkedHashMap<>();

    public SPRunes(IEventBus modEventBus) {
        modEventBus.addListener(this::onRegister);
        //Low priority so that Rune Enchanting has already added the Flame rune's lines
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, SPRunes::onTooltip);
    }

    /** Flame's own tooltip doesn't cover what it does on shields, so the blocking effect is added right below it. */
    private static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ShieldItem) || !RuneFeature.showExtraInfos(event.getFlags()))
            return;

        int level = RuneCompat.getRuneLevel(stack, SPEnchantments.ABLAZE);
        if (level <= 0)
            return;

        List<Component> tooltip = event.getToolTip();
        Component line = CommonComponents.space()
                .append(Component.translatable("rune.shieldsplus.flame.shield_info", InsaneLib.ONE_DECIMAL_FORMATTER.format(level * SPFeature.enchantments$ablazeTimeOnFire)))
                .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);

        String flameInfo = CommonComponents.space().append(RERunes.FLAME.value().getInfoComponent()).getString();
        for (int i = 0; i < tooltip.size(); i++) {
            if (tooltip.get(i).getString().equals(flameInfo)) {
                tooltip.add(i + 1, line);
                return;
            }
        }
        tooltip.add(line);
    }

    private void onRegister(RegisterEvent event) {
        event.register(RERunes.REGISTRY_KEY, helper -> {
            register(helper, SPEnchantments.RECOIL, "recoil", new RecoilRune());
            register(helper, SPEnchantments.REINFORCED, "reinforced", new ReinforcedRune());
            register(helper, SPEnchantments.AEGIS, "aegis", new AegisRune());
            register(helper, SPEnchantments.LIGHTWEIGHT, "lightweight", new LightweightRune());
            register(helper, SPEnchantments.FAST_RECOVERY, "fast_recovery", new FastRecoveryRune());
            register(helper, SPEnchantments.CELESTIAL_GUARDIAN, "celestial_guardian", new CelestialGuardianRune());
            //The Ablaze rune has been replaced by Rune Enchanting's Flame, items with Ablaze are loaded with Flame
            RERunes.REGISTRY.addAlias(ShieldsPlus.id("ablaze"), RERunes.FLAME.getId());
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
        Holder<Rune> holder = getRuneHolder(enchantment);
        return holder != null && RuneHelper.hasRune(stack, holder) ? levelEquivalent : 0;
    }

    @Nullable
    private static Holder<Rune> getRuneHolder(ResourceKey<Enchantment> enchantment) {
        //Ablaze has no rune of its own: Rune Enchanting's Flame acts as its rune when applied to a shield
        if (enchantment.equals(SPEnchantments.ABLAZE))
            return RERunes.FLAME;

        Rune rune = RUNES.get(enchantment);
        return rune == null ? null : RERunes.REGISTRY.wrapAsHolder(rune);
    }

    public static void tryTriggerThorns(LivingShieldBlockEvent event) {
        LivingEntity blockingEntity = event.getEntity();
        ItemStack useItem = blockingEntity.getUseItem();
        if (!RuneHelper.hasRune(useItem, RERunes.THORNS) || !(blockingEntity.level() instanceof ServerLevel serverLevel))
            return;

        RERunes.THORNS.value().onPostAttack(serverLevel, useItem, EnchantmentTarget.VICTIM, blockingEntity, event.getDamageSource());
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

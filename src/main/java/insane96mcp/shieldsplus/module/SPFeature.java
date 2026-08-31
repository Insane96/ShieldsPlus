package insane96mcp.shieldsplus.module;

import insane96mcp.insanelib.core.feature.Feature;
import insane96mcp.insanelib.core.feature.LoadFeature;
import insane96mcp.insanelib.core.feature.Module;
import insane96mcp.insanelib.core.feature.config.Config;
import insane96mcp.insanelib.util.IntegratedPack;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.event.SPEventFactory;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.shieldsplus.world.item.enchantment.AegisEnchantmentEffect;
import insane96mcp.shieldsplus.world.item.enchantment.CelestialGuardianEnchantmentEffect;
import insane96mcp.shieldsplus.world.item.enchantment.ReinforcedEnchantmentEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@LoadFeature(canBeDisabled = false)
public class SPFeature extends Feature {
    public static final TagKey<Item> REQUIRE_TWO_HANDS_ITEM_TAG = TagKey.create(Registries.ITEM, ShieldsPlus.id("requires_two_hands"));

    @Config(min = 0, description = "In vanilla when you start blocking with a shield, there's a 0.25 seconds (5 ticks) window where you are still not blocking. By default the windup is removed.")
    public static Integer shieldWindup = 0;
    @Config(min = 0d, max = 360d, description = "The total width (in degrees) of the frontal arc in which attacks can be blocked with a shield. Vanilla is 180 (i.e. 90 degrees either side of where you're looking).")
    public static Double blockingAngle = 100d;
    @Config(description = "If true shields will block only a certain amount of damage. If false the vanilla behaviour is used.")
    public static Boolean shieldBlockFixedDamageAmount = true;
    @Config(min = 0d, max = Float.MAX_VALUE, description = "The minimum damage dealt to the player for the shield to take damage. Vanilla is 3. E.g. With this set to 3, the shield will not be damaged if damage received is lower than 3.")
    public static Double minShieldHurtDamage = 0d;
    @Config(description = "Makes shields always disable for 1.6 seconds like Combat Test snapshots.")
    public static Boolean combatTestShieldDisabling = true;
    @Config(description = "If true, crouching will block with the shield and you can attack while blocking.")
    public static Boolean blockWithCrouch = true;
    @Config(description = "When crouch blocking, after attacking, how many seconds will the shield be in cooldown for?")
    public static Double blockWithCrouchExposure = 0.35d;
    @Config(description = "If true, shields can be lifted only for a certain amount of time and will go on cooldown.")
    public static Boolean liftedAndCooldown = true;
    @Config(min = 0, max = 1, description = "When shields go on cooldown, the time is given by how much time the shield has been blocking. This defines the minimum cooldown in percentage for the shield to go on cooldown (e.g. if you just block for a few ticks, the cooldown will be 30% of the shield's cooldown).")
    public static Double minCooldown = 0.3d;
    @Config(min = 0, description = "How many seconds after lifting a shield to make a parry count as such")
    public static Double parry$window = 0.1d;
    @Config(min = 0, description = "How much more damage (in percentage) is blocked if the player lifts the shield as soon as it's about to take damage?")
    public static Double parry$bonusDamageBlocked = 1d;

    @Config(description = "If true, enables a data pack that overrides the vanilla shield recipe with the mod's iron one.")
    public static Boolean overrideVanillaShieldRecipe = true;

    @Config(min = 0, description = "Amount of knockback given to entities per level.")
    public static Double enchantments$recoilEntitiesKnockback = 0.6d;
    @Config(min = 0, description = "Amount of knockback given to projectiles per level.")
    public static Double enchantments$recoilProjectilesKnockback = 5d;
    @Config(min = 0, max = 1, description = "Percentage bonus amount of damage blocked.")
    public static Double enchantments$reinforcedBlockedDamageBonus = 0.1d;
    @Config(min = 0, max = 1, description = "How much damage will the aegis enchantment negate per level.")
    public static Double enchantments$aegisPercentageDamageReduction = 0.1d;
    @Config(min = 1, description = "How many seconds will ablaze set entities on fire per level.")
    public static Integer enchantments$ablazeTimeOnFire = 2;
    @Config(min = 0, description = "Percentage increase of speed when blocking with the Lightweight enchantment.")
    public static Double enchantments$lightweightBonusSpeed = 2d;
    @Config(min = 0, max = 1, description = "Percentage cooldown reduction with the Fast Recovery enchantment.")
    public static Double enchantments$fastRecoveryCooldownReduction = 0.35d;

    @Override
    public void init(Module module, boolean enabledByDefault, boolean canBeDisabled) {
        super.init(module, enabledByDefault, canBeDisabled);
        IntegratedPack.addServerPack(ShieldsPlus.MOD_ID, "vanilla_shield_override", "Shields+ Vanilla Shield Override", () -> overrideVanillaShieldRecipe);
        CelestialGuardianEnchantmentEffect.LAST_ARMED_AT = this.createDataKey("celestial_guardian_last_armed_at");
        CelestialGuardianEnchantmentEffect.ARMED_DAMAGE_AMOUNT = this.createDataKey("celestial_guardian_armed_damage_amount");
    }

    @SubscribeEvent
    public void onShieldBlock(LivingShieldBlockEvent event) {
        if (!event.getOriginalBlock())
            return;

        ItemStack useItem = event.getEntity().getUseItem();
        RegistryLookup<Enchantment> enchantmentLookup = event.getEntity().level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        if (shieldBlockFixedDamageAmount) {
            float blockedDamage = SPShieldItem.getBlockedDamage(useItem);
            if (event.getEntity().getTicksUsingItem() <= parry$window * 20)
                blockedDamage *= (float) (1 + parry$bonusDamageBlocked);
            blockedDamage = ReinforcedEnchantmentEffect.increaseDamageBlocked(useItem, blockedDamage, enchantmentLookup);
            event.setBlockedDamage(blockedDamage);
        }

        //Process blocking enchantments
        if (useItem.getItem() instanceof ShieldItem) {
            useItem.getAllEnchantments(enchantmentLookup).entrySet().forEach(entry -> {
                Holder<Enchantment> enchantment = entry.getKey();
                int lvl = entry.getIntValue();
                enchantment.unwrapKey().map(SPEnchantments.BLOCKING_EFFECTS::get).ifPresent(blockingEffect ->
                        blockingEffect.onBlocked(event.getEntity(), event.getDamageSource(), event.getBlockedDamage(), lvl, event));
            });
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Pre event) {
        RegistryLookup<Enchantment> enchantmentLookup = event.getEntity().level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        AegisEnchantmentEffect.reduceDamage(event, enchantmentLookup);
        CelestialGuardianEnchantmentEffect.trackIncomingDamage(event.getEntity(), event.getNewDamage());
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (CelestialGuardianEnchantmentEffect.tryConsumeSave(event.getEntity())) {
            event.getEntity().setHealth(1f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!this.isEnabled())
            return;

        if (canBlockWithCrouch(event.getEntity())
                && !event.getEntity().isUsingItem()
                && event.getEntity().getOffhandItem().canPerformAction(ItemAbilities.SHIELD_BLOCK)
                && !CrossbowItem.isCharged(event.getEntity().getMainHandItem())
                && !event.getEntity().getMainHandItem().is(REQUIRE_TWO_HANDS_ITEM_TAG)) {
            event.getEntity().startUsingItem(InteractionHand.OFF_HAND);
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (!this.isEnabled()
                || !canBlockWithCrouch(event.getEntity())
                || !event.getEntity().isCrouching()
                || !event.getEntity().isUsingItem()
                || event.getEntity().getUsedItemHand() != InteractionHand.OFF_HAND
                || !event.getEntity().getOffhandItem().canPerformAction(ItemAbilities.SHIELD_BLOCK))
            return;

        event.getEntity().getCooldowns().addCooldown(event.getEntity().getOffhandItem().getItem(), (int) (blockWithCrouchExposure * 20));
        event.getEntity().stopUsingItem();
    }

    /**
     Returns true if the player can block while crouching
     */
    public static boolean canBlockWithCrouch(LivingEntity livingEntity) {
        if (!blockWithCrouch)
            return false;
        if (livingEntity instanceof Player player && player.getCooldowns().isOnCooldown(player.getOffhandItem().getItem()))
            return false;
        return livingEntity.getOffhandItem().canPerformAction(ItemAbilities.SHIELD_BLOCK) && livingEntity.isCrouching() && SPEventFactory.canBlockWithCrouch(livingEntity, livingEntity.getOffhandItem());
    }

    public static boolean combatTestShieldDisabling() {
        return isEnabled(SPFeature.class) && combatTestShieldDisabling;
    }
}
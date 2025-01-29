package insane96mcp.shieldsplus.module;

import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.LoadFeature;
import insane96mcp.insanelib.base.Module;
import insane96mcp.insanelib.base.config.Config;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.data.ShieldDefinition;
import insane96mcp.shieldsplus.data.ShieldDefinitionReloader;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.shieldsplus.world.item.enchantment.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

@Label(name = "Shields+")
@LoadFeature(module = ShieldsPlus.RESOURCE_PREFIX + "base", canBeDisabled = false)
public class BaseFeature extends Feature {
    public static final TagKey<Item> REQUIRE_TWO_HANDS_ITEM_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(ShieldsPlus.MOD_ID, "requires_two_hands"));

    @Config(min = 0)
    @Label(name = "Shield Windup", description = "In vanilla when you start blocking with a shield, there's a 0.25 seconds (5 ticks) window where you are still not blocking. By default the windup is removed.")
    public static Integer shieldWindup = 0;
    @Config
    @Label(name = "Shields Block Fixed Damage Amount", description = "If true shields will block only a certain amount of damage. If false the vanilla behaviour is used.")
    public static Boolean shieldBlockFixedDamageAmount = true;
    @Config(min = 0d, max = Float.MAX_VALUE)
    @Label(name = "Min Shield Hurt Damage", description = "The minimum damage dealt to the player for the shield to take damage. Vanilla is 3. E.g. With this set to 3, the shield will not be damaged if damage received is lower than.")
    public static Double minShieldHurtDamage = 0d;
    @Config
    @Label(name = "Combat Test shield disabling", description = "Makes shields always disable for 1.6 seconds like Combat Test snapshots.")
    public static Boolean combatTestShieldDisabling = true;
    @Config
    @Label(name = "Block with crouch", description = "If true, crouching will block with the shield and you can attack while blocking.")
    public static Boolean blockWithCrouch = true;
    @Config
    @Label(name = "Lifted and Cooldown", description = "If true, shields can be lifted only for a certain amount of time and will go on cooldown.")
    public static Boolean liftedAndCooldown = true;

    @Config(min = 1)
    @Label(name = "Enchantments.Ablaze Time on fire", description = "How many seconds will ablaze set entities on fire per level.")
    public static Integer enchantmentsAblazeTimeOnFire = 2;
    @Config(min = 0, max = 1)
    @Label(name = "Enchantments.Aegis percentage damage reduction", description = "How much damage will the aegis enchantment negate per level.")
    public static Double enchantmentsAegisPercentageDamageReduction = 0.1d;
    @Config(min = 0, max = 1)
    @Label(name = "Enchantments.Fast Recovery cooldown reduction", description = "Percentage cooldown reduction with the Fast Recovery enchantment.")
    public static Double enchantmentsFastRecoveryCooldownReduction = 0.35d;
    @Config(min = 0)
    @Label(name = "Enchantments.Lightweight bonus speed", description = "Percentage increase of speed when blocking with the Lightweight enchantment.")
    public static Double enchantmentsLightweightBonusSpeed = 2d;
    @Config(min = 0, max = 10)
    @Label(name = "Enchantments.Perfect Parry tick window", description = "Max ticks for a perfect parry.")
    public static Integer enchantmentsPerfectParryTickWindow = 1;
    @Config(min = 0)
    @Label(name = "Enchantments.Recoil entities knockback", description = "Amount of knockback given to entities per level.")
    public static Double enchantmentsRecoilEntitiesKnockback = 0.5d;
    @Config(min = 0)
    @Label(name = "Enchantments.Recoil projectiles knockback", description = "Amount of knockback given to projectiles per level.")
    public static Double enchantmentsRecoilProjectilesKnockback = 5d;
    @Config(min = 0, max = 1)
    @Label(name = "Enchantments.Reflection reflected damage", description = "Percentage amount of damage reflected.")
    public static Double enchantmentsReflectionReflectedDamage = 0.08d;
    @Config(min = 0, max = 1)
    @Label(name = "Enchantments.Reinforced blocked damage bonus", description = "Percentage bonus amount of damage blocked.")
    public static Double enchantmentsReinforcedBlockedDamageBonus = 0.1d;

    public BaseFeature(Module module, boolean enabledByDefault, boolean canBeDisabled) {
        super(module, enabledByDefault, canBeDisabled);
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        if (shieldBlockFixedDamageAmount) {
            if (event.getEntity().getUseItem().getItem() instanceof SPShieldItem spShieldItem) {
                float baseBlockedDamage = spShieldItem.getBlockedDamage(event.getEntity().getUseItem(), event.getEntity(), event.getEntity().level());
                float blockedDamage = ReinforcedEnchantment.increaseDamageBlocked(event.getEntity().getUseItem(), baseBlockedDamage);
                event.setBlockedDamage(blockedDamage);
            }
            else {
                Optional<ShieldDefinition> shieldDefinition = ShieldDefinitionReloader.getShieldDefinition(event.getEntity().getUseItem());
                shieldDefinition.ifPresent(def -> {
                    float blockedDamage = ReinforcedEnchantment.increaseDamageBlocked(event.getEntity().getUseItem(), def.blockedDamage);
                    event.setBlockedDamage(blockedDamage);
                });
            }
        }

        //Process blocking enchantments
        if (event.getEntity().getUseItem().getItem() instanceof ShieldItem) {
            event.getEntity().getUseItem().getAllEnchantments().forEach((enchantment, lvl) -> {
                if (enchantment instanceof IBlockingEffect blockingEffectEnchantment)
                    blockingEffectEnchantment.onBlocked(event.getEntity(), event.getDamageSource(), event.getBlockedDamage(), lvl, event);
            });
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        AegisEnchantment.reduceDamage(event);
        CelestialGuardianEnchantment.trySaveAmount(event.getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (CelestialGuardianEnchantment.tryApply(event.getEntity())) {
            event.getEntity().setHealth(1f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!this.isEnabled())
            return;

        if (event.phase == TickEvent.Phase.END) {
            LightweightEnchantment.onTick(event.player);
            ShieldBashEnchantment.onTick(event.player);
        }

        if (canBlockWithCrouch(event.player)
                && !event.player.isUsingItem()
                && event.player.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK)
                && !CrossbowItem.isCharged(event.player.getMainHandItem())
                && !event.player.getMainHandItem().is(REQUIRE_TWO_HANDS_ITEM_TAG)) {
            event.player.startUsingItem(InteractionHand.OFF_HAND);
        }
        /*if (blockWithCrouch && event.player.isUsingItem() && event.player.getUseItem().canPerformAction(ToolActions.SHIELD_BLOCK) && event.player.isCrouching() && event.player.getCooldowns().isOnCooldown(event.player.getOffhandItem().getItem())) {
            event.player.stopUsingItem();
        }*/
    }

    /**
     Returns true if the player can block while crouching
     */
    public static boolean canBlockWithCrouch(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && player.getCooldowns().isOnCooldown(player.getOffhandItem().getItem()))
            return false;
        return blockWithCrouch && livingEntity.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK) && livingEntity.isCrouching();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!this.isEnabled()
                || !shieldBlockFixedDamageAmount
                || event.getItemStack().getItem() instanceof SPShieldItem)
            return;

        Optional<ShieldDefinition> shieldDefinition = ShieldDefinitionReloader.getShieldDefinition(event.getItemStack());
        if (shieldDefinition.isEmpty())
            return;
        SPShieldItem.addDamageBlockedText(event.getItemStack(), event.getToolTip(), shieldDefinition.get().blockedDamage);
    }

    public static boolean shouldRemoveShieldWindup() {
        return isEnabled(BaseFeature.class) && shieldWindup != 5;
    }

    public static boolean combatTestShieldDisabling() {
        return isEnabled(BaseFeature.class) && combatTestShieldDisabling;
    }
}
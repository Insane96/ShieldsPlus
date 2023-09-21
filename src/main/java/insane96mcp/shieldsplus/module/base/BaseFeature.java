package insane96mcp.shieldsplus.module.base;

import insane96mcp.insanelib.base.Feature;
import insane96mcp.insanelib.base.Label;
import insane96mcp.insanelib.base.Module;
import insane96mcp.insanelib.base.config.Config;
import insane96mcp.insanelib.base.config.LoadFeature;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPShieldMaterials;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.shieldsplus.world.item.enchantment.IBlockingEffect;
import insane96mcp.shieldsplus.world.item.enchantment.LightweightEnchantment;
import insane96mcp.shieldsplus.world.item.enchantment.ReinforcedEnchantment;
import insane96mcp.shieldsplus.world.item.enchantment.ShieldBashEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Label(name = "Shields+")
@LoadFeature(module = ShieldsPlus.RESOURCE_PREFIX + "base", canBeDisabled = false)
public class BaseFeature extends Feature {
    @Config(min = 0)
    @Label(name = "Shield Windup", description = "In vanilla when you start blocking with a shield, there's a 0.25 seconds (5 ticks) window where you are still not blocking. By default is now set to 0.1 seconds.")
    public static Integer shieldWindup = 2;
    @Config
    @Label(name = "Shields Block Fixed Damage Amount", description = "If true shields will block only a certain amount of damage. If false the vanilla behaviour is used.")
    public static Boolean shieldBlockFixedDamageAmount = true;
    @Config(min = 0d, max = Float.MAX_VALUE)
    @Label(name = "Min Shield Hurt Damage", description = "The minimum damage dealt to the player for the shield to take damage. Vanilla is 3. E.g. With this set to 3, the shield will not be damaged if damage received is lower than.")
    public static Double minShieldHurtDamage = 0d;
    @Config
    @Label(name = "Combat Test shield disabling", description = "Makes shields always disable for 1.6 seconds like Combat Test snapshots.")
    public static Boolean combatTestShieldDisabling = true;

    public BaseFeature(Module module, boolean enabledByDefault, boolean canBeDisabled) {
        super(module, enabledByDefault, canBeDisabled);
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        if (shieldBlockFixedDamageAmount) {
            double baseBlockedDamage;
            if (event.getEntity().getUseItem().is(Items.SHIELD)) {
                baseBlockedDamage = SPShieldMaterials.IRON.damageBlocked;
            }
            else if (event.getEntity().getUseItem().getItem() instanceof SPShieldItem) {
                baseBlockedDamage = ((SPShieldItem) event.getEntity().getUseItem().getItem()).getBlockedDamage(event.getEntity().getUseItem(), event.getEntity(), event.getEntity().level());
            }
            else
                return;
            float blockedDamage = (float) (baseBlockedDamage + ReinforcedEnchantment.getDamageBlocked(event.getEntity().getUseItem()));
            event.setBlockedDamage(blockedDamage);
        }

        processEnchantments(event.getEntity(), event.getDamageSource(), event.getOriginalBlockedDamage());
    }

    private void processEnchantments(LivingEntity blockingEntity, DamageSource source, float amount) {
        if (blockingEntity.getUseItem().getItem() instanceof ShieldItem) {
            blockingEntity.getUseItem().getAllEnchantments().forEach((enchantment, lvl) -> {
                if (enchantment instanceof IBlockingEffect blockingEffectEnchantment)
                    blockingEffectEnchantment.onBlocked(blockingEntity, source, amount, lvl);
            });
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!this.isEnabled()
                || event.phase != TickEvent.Phase.END)
            return;

        LightweightEnchantment.onTick(event.player);
        ShieldBashEnchantment.onTick(event.player);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!this.isEnabled()
                || !shieldBlockFixedDamageAmount)
            return;

        if (event.getItemStack().is(Items.SHIELD)) {
            SPShieldItem.addDamageBlockedText(event.getItemStack(), event.getToolTip(), SPShieldMaterials.IRON.damageBlocked);
        }
    }

    public static boolean shouldRemoveShieldWindup() {
        return isEnabled(BaseFeature.class) && shieldWindup != 5;
    }

    public static boolean combatTestShieldDisabling() {
        return isEnabled(BaseFeature.class) && combatTestShieldDisabling;
    }
}
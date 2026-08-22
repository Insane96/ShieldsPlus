package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.util.MCUtils;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * Not an {@link IBlockingEnchantmentEffect}: the speed bonus is a per-tick attribute modifier rather than
 * a reaction to a block landing, so it's driven from {@link SPFeature}'s player tick handler instead.
 */
public class LightweightEnchantmentEffect {
    public static final ResourceLocation BONUS_SPEED_ID = ShieldsPlus.id("lightweight_bonus_speed");

    public static void onTick(Player player) {
        if (player.isBlocking() && !SPFeature.canBlockWithCrouch(player)) {
            int lvl = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(SPEnchantments.LIGHTWEIGHT)
                    .map(player.getUseItem()::getEnchantmentLevel)
                    .orElse(0);
            if (lvl > 0)
                MCUtils.applyModifier(player, Attributes.MOVEMENT_SPEED, BONUS_SPEED_ID, SPFeature.enchantments$lightweightBonusSpeed * lvl, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, false);
        }
        else {
            AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null)
                attribute.removeModifier(BONUS_SPEED_ID);
        }
    }
}

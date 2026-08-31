package insane96mcp.shieldsplus.world.item.enchantment;

import insane96mcp.insanelib.event.PlayerUseItemMovSpeedEvent;
import insane96mcp.shieldsplus.module.RuneCompat;
import insane96mcp.shieldsplus.module.SPFeature;
import insane96mcp.shieldsplus.setup.SPEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Not an {@link IBlockingEnchantmentEffect}: the speed bonus applies to the client-only movement speed
 * multiplier used while using an item, so it's driven from {@link PlayerUseItemMovSpeedEvent} instead
 * (registered client-side only, see {@link insane96mcp.shieldsplus.ShieldsPlus}).
 */
public class LightweightEnchantmentEffect {

    public static void onUseItemMovSpeed(PlayerUseItemMovSpeedEvent event) {
        Player player = event.getPlayer();
        if (!player.isBlocking() || SPFeature.canBlockWithCrouch(player))
            return;

        ItemStack useItem = event.getUseItem();
        int lvl = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(SPEnchantments.LIGHTWEIGHT)
                .map(useItem::getEnchantmentLevel)
                .orElse(0);
        lvl += RuneCompat.getRuneLevel(useItem, SPEnchantments.LIGHTWEIGHT);
        if (lvl <= 0)
            return;

        event.setSpeedModifier((float) (PlayerUseItemMovSpeedEvent.VANILLA_SPEED_MODIFIER * (1 + SPFeature.enchantments$lightweightBonusSpeed * lvl)));
    }
}

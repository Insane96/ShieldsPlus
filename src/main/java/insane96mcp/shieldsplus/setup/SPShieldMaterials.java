package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.world.item.SPShieldMaterial;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

public class SPShieldMaterials {
    public static final SPShieldMaterial WOODEN = new SPShieldMaterial("wooden", 1, 84, ItemTags.PLANKS, Tiers.WOOD.getEnchantmentValue(), Rarity.COMMON);
    public static final SPShieldMaterial STONE = new SPShieldMaterial("stone", 1.5, 112, ItemTags.STONE_TOOL_MATERIALS, Tiers.STONE.getEnchantmentValue(), Rarity.COMMON);
    public static final SPShieldMaterial GOLDEN = new SPShieldMaterial("golden", 1, 42, () -> Items.GOLD_INGOT, Tiers.GOLD.getEnchantmentValue(), Rarity.COMMON);
    public static final SPShieldMaterial IRON = new SPShieldMaterial("iron", 3, 332, () -> Items.IRON_INGOT, Tiers.IRON.getEnchantmentValue(), Rarity.COMMON);
    public static final SPShieldMaterial DIAMOND = new SPShieldMaterial("diamond", 5, 588, () -> Items.DIAMOND, Tiers.DIAMOND.getEnchantmentValue(), Rarity.COMMON);
    public static final SPShieldMaterial NETHERITE = new SPShieldMaterial("netherite", 6, 672, () -> Items.NETHERITE_INGOT, Tiers.NETHERITE.getEnchantmentValue(), Rarity.COMMON);
}

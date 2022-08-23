package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.world.item.SPShieldMaterial;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class SPShieldMaterials {
    public static final SPShieldMaterial WOODEN = new SPShieldMaterial("wooden", 1, 84, ItemTags.PLANKS, 15, Rarity.COMMON);
    public static final SPShieldMaterial STONE = new SPShieldMaterial("stone", 2, 112, ItemTags.STONE_TOOL_MATERIALS, 5, Rarity.COMMON);
    public static final SPShieldMaterial COPPER = new SPShieldMaterial("copper", 3, 134, Items.COPPER_INGOT, 10, Rarity.COMMON);
    public static final SPShieldMaterial GOLDEN = new SPShieldMaterial("golden", 1, 42, Items.GOLD_INGOT, 22, Rarity.COMMON);
    public static final SPShieldMaterial IRON = new SPShieldMaterial("iron", 5, 332, Items.IRON_INGOT, 14, Rarity.COMMON);
    public static final SPShieldMaterial DIAMOND = new SPShieldMaterial("diamond", 7, 588, Items.DIAMOND, 10, Rarity.COMMON);
    public static final SPShieldMaterial NETHERITE = new SPShieldMaterial("netherite", 8, 672, Items.NETHERITE_INGOT, 15, Rarity.COMMON);
}

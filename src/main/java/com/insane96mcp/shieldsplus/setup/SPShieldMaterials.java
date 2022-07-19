package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class SPShieldMaterials {
    public static final Material WOODEN = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/wooden_shield_base"));
    public static final Material WOODEN_NO_PATTERN = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/wooden_shield_base_nopattern"));
}

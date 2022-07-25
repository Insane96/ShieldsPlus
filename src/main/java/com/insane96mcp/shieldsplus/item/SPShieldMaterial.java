package com.insane96mcp.shieldsplus.item;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Rarity;

public class SPShieldMaterial {
    public Material material;
    public Material noPatternMaterial;
    public double damageBlocked;
    public int durability;
    public Rarity rarity;

    public SPShieldMaterial(String material, double damageBlocked, int durability, Rarity rarity) {
        this.material = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield".formatted(material)));
        this.noPatternMaterial = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield_nopattern".formatted(material)));
        this.damageBlocked = damageBlocked;
        this.durability = durability;
        this.rarity = rarity;
    }
}

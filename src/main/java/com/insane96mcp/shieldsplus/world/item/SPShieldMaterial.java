package com.insane96mcp.shieldsplus.world.item;

import net.minecraft.world.item.Rarity;

public class SPShieldMaterial {
    //public Material material;
    //public Material noPatternMaterial;
    public String materialName;
    public double damageBlocked;
    public int durability;
    public Rarity rarity;

    public SPShieldMaterial(String materialName, double damageBlocked, int durability, Rarity rarity) {
        //this.material = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield".formatted(material)));
        //this.noPatternMaterial = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ShieldsPlus.MOD_ID, "entity/%s_shield_nopattern".formatted(material)));
        this.materialName = materialName;
        this.damageBlocked = damageBlocked;
        this.durability = durability;
        this.rarity = rarity;
    }
}

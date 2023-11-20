package insane96mcp.shieldsplus.world.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class SPShieldMaterial {
    public String materialName;
    public int durability;
    public Supplier<Item> repairItem;
    public TagKey<Item> repairTag;
    public int enchantmentValue;
    public Rarity rarity;

    public SPShieldMaterial(String materialName, int durability, Supplier<Item> repairItem, int enchantmentValue, Rarity rarity) {
        this.materialName = materialName;
        this.durability = durability;
        this.repairItem = repairItem;
        this.enchantmentValue = enchantmentValue;
        this.rarity = rarity;
    }

    public SPShieldMaterial(String materialName, int durability, TagKey<Item> repairTag, int enchantmentValue, Rarity rarity) {
        this.materialName = materialName;
        this.durability = durability;
        this.repairTag = repairTag;
        this.enchantmentValue = enchantmentValue;
        this.rarity = rarity;
    }
}

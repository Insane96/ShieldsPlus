package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.world.item.SPShieldItem;
import com.insane96mcp.shieldsplus.world.item.SPShieldMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShieldsPlus.MOD_ID);
    public static final List<RegistryObject<SPShieldItem>> SHIELDS = new ArrayList<>();

    public static final RegistryObject<SPShieldItem> WOODEN_SHIELD = registerShield(Strings.Items.WOODEN_SHIELD, SPShieldMaterials.WOODEN);
    public static final RegistryObject<SPShieldItem> STONE_SHIELD = registerShield(Strings.Items.STONE_SHIELD, SPShieldMaterials.STONE);
    public static final RegistryObject<SPShieldItem> COPPER_SHIELD = registerShield(Strings.Items.COPPER_SHIELD, SPShieldMaterials.COPPER);
    public static final RegistryObject<SPShieldItem> GOLDEN_SHIELD = registerShield(Strings.Items.GOLDEN_SHIELD, SPShieldMaterials.GOLDEN);
    public static final RegistryObject<SPShieldItem> DIAMOND_SHIELD = registerShield(Strings.Items.DIAMOND_SHIELD, SPShieldMaterials.DIAMOND);
    public static final RegistryObject<SPShieldItem> NETHERITE_SHIELD = registerShield(Strings.Items.NETHERITE_SHIELD, SPShieldMaterials.NETHERITE);


    public static RegistryObject<SPShieldItem> registerShield(String id, SPShieldMaterial material) {
        RegistryObject<SPShieldItem> shield = ITEMS.register(id, () -> new SPShieldItem(material, new Item.Properties().durability(material.durability).tab(CreativeModeTab.TAB_COMBAT).rarity(material.rarity)));
        SHIELDS.add(shield);
        return shield;
    }
}
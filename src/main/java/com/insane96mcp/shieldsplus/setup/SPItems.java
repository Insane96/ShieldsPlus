package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.item.SPShieldItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShieldsPlus.MOD_ID);

    //1.344
    public static final RegistryObject<SPShieldItem> WOODEN_SHIELD = ITEMS.register(Strings.Items.WOODEN_SHIELD, () -> new SPShieldItem(1d, new Item.Properties().durability(84).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<SPShieldItem> STONE_SHIELD = ITEMS.register(Strings.Items.STONE_SHIELD, () -> new SPShieldItem(3d, new Item.Properties().durability(112).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<SPShieldItem> GOLDEN_SHIELD = ITEMS.register(Strings.Items.GOLDEN_SHIELD, () -> new SPShieldItem(1d, new Item.Properties().durability(42).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<SPShieldItem> DIAMOND_SHIELD = ITEMS.register(Strings.Items.DIAMOND_SHIELD, () -> new SPShieldItem(7d, new Item.Properties().durability(588).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<SPShieldItem> NETHERITE_SHIELD = ITEMS.register(Strings.Items.NETHERITE_SHIELD, () -> new SPShieldItem(8d, new Item.Properties().durability(672).tab(CreativeModeTab.TAB_COMBAT)));
}
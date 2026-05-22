package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ShieldsPlus.MOD_ID);
    public static final List<DeferredHolder<Item, SPShieldItem>> SHIELDS = new ArrayList<>();

    public static final DeferredHolder<Item, SPShieldItem> WOODEN_SHIELD = registerShield("wooden_shield", new Item.Properties().durability(84), 2f, 5f, 0.75f, Ingredient.of(ItemTags.PLANKS));
    public static final DeferredHolder<Item, SPShieldItem> STONE_SHIELD = registerShield("stone_shield", new Item.Properties().durability(112), 3f, 4f, 1.25f, Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));
    public static final DeferredHolder<Item, SPShieldItem> IRON_SHIELD = registerShield("iron_shield", new Item.Properties().durability(332), 4f, 3f, 2f, Ingredient.of(Items.IRON_INGOT));
    public static final DeferredHolder<Item, SPShieldItem> GOLDEN_SHIELD = registerShield("golden_shield", new Item.Properties().durability(42), 2f, 6f, 0.5f, Ingredient.of(Items.GOLD_INGOT));
    public static final DeferredHolder<Item, SPShieldItem> DIAMOND_SHIELD = registerShield("diamond_shield", new Item.Properties().durability(588), 5f, 2.5f, 2.5f, Ingredient.of(Items.DIAMOND));
    public static final DeferredHolder<Item, SPShieldItem> NETHERITE_SHIELD = registerShield("netherite_shield", new Item.Properties().durability(672), 5.5f, 2f, 3f, Ingredient.of(Items.NETHERITE_INGOT));

    public static DeferredHolder<Item, SPShieldItem> registerShield(String id, Item.Properties properties, float damageBlocked, float blockingTime, float cooldown, Ingredient repairIngredient) {
        DeferredHolder<Item, SPShieldItem> shield = ITEMS.register(id, () -> new SPShieldItem(properties, damageBlocked, blockingTime, cooldown, repairIngredient));
        SHIELDS.add(shield);
        return shield;
    }
}
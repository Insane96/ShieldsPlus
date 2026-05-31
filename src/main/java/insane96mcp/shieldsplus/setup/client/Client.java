package insane96mcp.shieldsplus.setup.client;

import insane96mcp.shieldsplus.setup.SPItems;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Client {

    public static void setup(final FMLClientSetupEvent event) {
        initShields();
    }

    private static void initShields() {
        //noinspection deprecation
        ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        for (DeferredHolder<Item, SPShieldItem> shieldItem : SPItems.SHIELDS) {
            ItemProperties.register(shieldItem.get(), ResourceLocation.withDefaultNamespace("blocking"), blockFn);
        }
    }

    public static void creativeTabsBuildContents(final BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            addBefore(event, Items.SHIELD, SPItems.WOODEN_SHIELD.get());
            addBefore(event, Items.SHIELD, SPItems.STONE_SHIELD.get());
            addBefore(event, Items.SHIELD, SPItems.COPPER_SHIELD.get());
            addBefore(event, Items.SHIELD, SPItems.IRON_SHIELD.get());

            addAfter(event, Items.SHIELD, SPItems.NETHERITE_SHIELD.get());
            addAfter(event, Items.SHIELD, SPItems.DIAMOND_SHIELD.get());
            addAfter(event, Items.SHIELD, SPItems.GOLDEN_SHIELD.get());
            event.remove(new ItemStack(Items.SHIELD), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private static void addBefore(BuildCreativeModeTabContentsEvent event, Item before, Item itemToAdd) {
        event.insertBefore(new ItemStack(before), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void addAfter(BuildCreativeModeTabContentsEvent event, Item after, Item itemToAdd) {
        event.insertAfter(new ItemStack(after), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}

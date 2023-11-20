package insane96mcp.shieldsplus.setup.client;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.setup.SPItems;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ShieldsPlus.MOD_ID)
public class Client {

    @SuppressWarnings("unused")
    public static void setup(final FMLClientSetupEvent event) {
        initShields();

        DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Minecraft.getInstance().getLocale());
        ShieldsPlus.ONE_DECIMAL_FORMATTER = new DecimalFormat("#.#", DECIMAL_FORMAT_SYMBOLS);
    }

/*    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            shieldItem.get().initClientMaterial();
            event.addSprite(shieldItem.get().clientMaterial.texture());
            event.addSprite(shieldItem.get().clientMaterialNoPattern.texture());
        }
    }*/

    private static void initShields() {
        //noinspection deprecation
        ItemPropertyFunction blockFn = (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        for (RegistryObject<SPShieldItem> shieldItem : SPItems.SHIELDS) {
            ItemProperties.register(shieldItem.get(), SPShieldItem.BLOCKING, blockFn);
        }
    }

    public static void creativeTabsBuildContents(final BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            addBefore(event, Items.SHIELD, SPItems.WOODEN_SHIELD.get());
            addBefore(event, Items.SHIELD, SPItems.STONE_SHIELD.get());
            addBefore(event, Items.SHIELD, SPItems.IRON_SHIELD.get());

            addAfter(event, Items.SHIELD, SPItems.NETHERITE_SHIELD.get());
            addAfter(event, Items.SHIELD, SPItems.DIAMOND_SHIELD.get());
            addAfter(event, Items.SHIELD, SPItems.GOLDEN_SHIELD.get());
            event.getEntries().remove(new ItemStack(Items.SHIELD));
        }
    }

    private static void addBefore(BuildCreativeModeTabContentsEvent event, Item before, Item itemToAdd) {
        event.getEntries().putBefore(new ItemStack(before), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void addAfter(BuildCreativeModeTabContentsEvent event, Item after, Item itemToAdd) {
        event.getEntries().putAfter(new ItemStack(after), new ItemStack(itemToAdd), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}

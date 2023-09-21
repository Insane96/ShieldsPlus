package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import insane96mcp.shieldsplus.world.item.SPShieldMaterial;
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

    public static final RegistryObject<SPShieldItem> WOODEN_SHIELD = registerShield("wooden_shield", SPShieldMaterials.WOODEN);
    public static final RegistryObject<SPShieldItem> STONE_SHIELD = registerShield("stone_shield", SPShieldMaterials.STONE);
    public static final RegistryObject<SPShieldItem> IRON_SHIELD = registerShield("iron_shield", SPShieldMaterials.IRON);
    public static final RegistryObject<SPShieldItem> GOLDEN_SHIELD = registerShield("golden_shield", SPShieldMaterials.GOLDEN);
    public static final RegistryObject<SPShieldItem> DIAMOND_SHIELD = registerShield("diamond_shield", SPShieldMaterials.DIAMOND);
    public static final RegistryObject<SPShieldItem> NETHERITE_SHIELD = registerShield("netherite_shield", SPShieldMaterials.NETHERITE);

    public static RegistryObject<SPShieldItem> registerShield(String id, SPShieldMaterial material) {
        RegistryObject<SPShieldItem> shield = ITEMS.register(id, () -> new SPShieldItem(material, new Item.Properties().durability(material.durability).rarity(material.rarity)));
        SHIELDS.add(shield);
        return shield;
    }
}
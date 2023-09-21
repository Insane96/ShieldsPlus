package insane96mcp.shieldsplus.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import insane96mcp.insanelib.util.LogHelper;
import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.network.ClientUpdateShieldDefinitionMessage;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ShieldsPlus.MOD_ID)
public class ShieldDefinitionReloader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    public static final ShieldDefinitionReloader INSTANCE;

    public static final List<ShieldDefinition> SHIELD_DEFINITIONS = new ArrayList<>();

    public ShieldDefinitionReloader() {
        super(GSON, "shield_definitions");
    }

    static {
        INSTANCE = new ShieldDefinitionReloader();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        SHIELD_DEFINITIONS.clear();
        try {
            map.forEach((resourceLocation, jElement) -> {
                ShieldDefinition shieldDefinition = GSON.fromJson(jElement, ShieldDefinition.class);
                shieldDefinition.id = resourceLocation;
                apply(shieldDefinition);
                SHIELD_DEFINITIONS.add(shieldDefinition);
            });
        }
        catch (JsonSyntaxException e) {
            LogHelper.error("Parsing error loading Shield Definition: %s", e);
        }
        catch (Exception e) {
            LogHelper.error("Failed loading Shield Definition: %s", e);
        }
    }

    public static void apply(List<ShieldDefinition> map) {
        map.forEach(ShieldDefinitionReloader::apply);
    }

    public static void apply(ShieldDefinition shieldDefinition) {
        Item item = ForgeRegistries.ITEMS.getValue(shieldDefinition.id);
        if (!(item instanceof SPShieldItem spShieldItem))
            return;

        spShieldItem.blockingDamageOverride = shieldDefinition.blockedDamage;
    }

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            event.getPlayerList().getPlayers().forEach(player -> ClientUpdateShieldDefinitionMessage.sync(SHIELD_DEFINITIONS, player));
        }
        else {
            ClientUpdateShieldDefinitionMessage.sync(SHIELD_DEFINITIONS, event.getPlayer());
        }
    }
}

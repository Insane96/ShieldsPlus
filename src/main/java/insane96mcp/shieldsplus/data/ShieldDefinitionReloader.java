package insane96mcp.shieldsplus.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import insane96mcp.insanelib.util.LogHelper;
import insane96mcp.shieldsplus.world.item.SPShieldItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ShieldDefinitionReloader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    public static final ShieldDefinitionReloader INSTANCE;

    public ShieldDefinitionReloader() {
        super(GSON, "shield_definitions");
    }

    static {
        INSTANCE = new ShieldDefinitionReloader();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        try {
            map.forEach((resourceLocation, jElement) -> {
                ShieldDefinition shieldDefinition = GSON.fromJson(jElement, ShieldDefinition.class);
                apply(resourceLocation, shieldDefinition);
            });
        }
        catch (JsonSyntaxException e) {
            LogHelper.error("Parsing error loading Shield Definition: %s", e);
        }
        catch (Exception e) {
            LogHelper.error("Failed loading Shield Definition: %s", e);
        }
    }

    public static void apply(Map<ResourceLocation, ShieldDefinition> map) {
        map.forEach(ShieldDefinitionReloader::apply);
    }

    public static void apply(ResourceLocation resourceLocation, ShieldDefinition shieldDefinition) {
        Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
        if (!(item instanceof SPShieldItem spShieldItem))
            return;

        spShieldItem.blockingDamageOverride = shieldDefinition.blockedDamage;
    }
}

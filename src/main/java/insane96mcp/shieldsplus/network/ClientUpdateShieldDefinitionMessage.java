package insane96mcp.shieldsplus.network;

import insane96mcp.shieldsplus.data.ShieldDefinition;
import insane96mcp.shieldsplus.data.ShieldDefinitionReloader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class ClientUpdateShieldDefinitionMessage {
    Map<ResourceLocation, ShieldDefinition> map;

    public ClientUpdateShieldDefinitionMessage(Map<ResourceLocation, ShieldDefinition> map) {
        this.map = map;
    }

    public static void encode(ClientUpdateShieldDefinitionMessage msg, FriendlyByteBuf buf) {
        buf.writeMap(msg.map, ClientUpdateShieldDefinitionMessage::resourceLocationToNetwork, ShieldDefinition::toNetwork);
    }

    private static void resourceLocationToNetwork(FriendlyByteBuf buf, ResourceLocation resourceLocation) {
        buf.writeUtf(resourceLocation.toString());
    }

    public static ClientUpdateShieldDefinitionMessage decode(FriendlyByteBuf buf) {
        return new ClientUpdateShieldDefinitionMessage(buf.readMap(ClientUpdateShieldDefinitionMessage::resourceLocationFromNetwork, ShieldDefinition::fromNetwork));
    }

    private static ResourceLocation resourceLocationFromNetwork(FriendlyByteBuf buf) {
        return ResourceLocation.tryParse(buf.readUtf());
    }


    public static void handle(final ClientUpdateShieldDefinitionMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ShieldDefinitionReloader.apply(message.map));
        ctx.get().setPacketHandled(true);
    }
}

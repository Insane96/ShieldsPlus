package insane96mcp.shieldsplus.network;

import insane96mcp.shieldsplus.data.ShieldDefinition;
import insane96mcp.shieldsplus.data.ShieldDefinitionReloader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ClientUpdateShieldDefinitionMessage {
    List<ShieldDefinition> shieldDefinitions;

    public ClientUpdateShieldDefinitionMessage(List<ShieldDefinition> shieldDefinitions) {
        this.shieldDefinitions = shieldDefinitions;
    }

    public static void encode(ClientUpdateShieldDefinitionMessage msg, FriendlyByteBuf buf) {
        buf.writeCollection(msg.shieldDefinitions, ShieldDefinition::toNetwork);
    }

    public static ClientUpdateShieldDefinitionMessage decode(FriendlyByteBuf buf) {
        return new ClientUpdateShieldDefinitionMessage(buf.readList(ShieldDefinition::fromNetwork));
    }

    public static void handle(final ClientUpdateShieldDefinitionMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ShieldDefinitionReloader.apply(message.shieldDefinitions));
        ctx.get().setPacketHandled(true);
    }

    public static void sync(List<ShieldDefinition> shieldDefinitions, ServerPlayer player) {
        Object msg = new ClientUpdateShieldDefinitionMessage(shieldDefinitions);
        NetworkHandler.CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

package insane96mcp.shieldsplus.network;

import insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(2);
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ShieldsPlus.MOD_ID, "network_channel"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    static int index = 0;
    public static void init() {
        CHANNEL.registerMessage(++index, ClientUpdateShieldDefinitionMessage.class, ClientUpdateShieldDefinitionMessage::encode, ClientUpdateShieldDefinitionMessage::decode, ClientUpdateShieldDefinitionMessage::handle);
    }
}

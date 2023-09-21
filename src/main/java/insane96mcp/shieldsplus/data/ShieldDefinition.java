package insane96mcp.shieldsplus.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ShieldDefinition {
    public transient ResourceLocation id;
    @SerializedName("blocked_damage")
    public float blockedDamage;

    public ShieldDefinition(ResourceLocation id, float blockedDamage) {
        this.id = id;
        this.blockedDamage = blockedDamage;
    }

    public static ShieldDefinition fromNetwork(FriendlyByteBuf buf) {
        return new ShieldDefinition(ResourceLocation.tryParse(buf.readUtf()), buf.readFloat());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(this.id.toString());
        buf.writeFloat(this.blockedDamage);
    }

    public static void toNetwork(FriendlyByteBuf buf, ShieldDefinition shieldDefinition) {
        shieldDefinition.toNetwork(buf);
    }
}

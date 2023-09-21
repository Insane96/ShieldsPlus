package insane96mcp.shieldsplus.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.network.FriendlyByteBuf;

public class ShieldDefinition {
    @SerializedName("blocked_damage")
    public float blockedDamage;

    public ShieldDefinition(float blockedDamage) {
        this.blockedDamage = blockedDamage;
    }

    public static ShieldDefinition fromNetwork(FriendlyByteBuf buf) {
        return new ShieldDefinition(buf.readFloat());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(this.blockedDamage);
    }

    public static void toNetwork(FriendlyByteBuf buf, ShieldDefinition shieldDefinition) {
        buf.writeFloat(shieldDefinition.blockedDamage);
    }
}

package insane96mcp.shieldsplus.data;

import com.google.gson.annotations.SerializedName;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ShieldDefinition {
    public transient ResourceLocation id;
    @SerializedName("blocked_damage")
    public float blockedDamage;
    @SerializedName("blocking_ticks")
    public int blockingTicks;
    @SerializedName("cooldown")
    public int cooldownTicks;

    public ShieldDefinition(ResourceLocation id, float blockedDamage, int blockingTicks, int cooldownTicks) {
        this.id = id;
        this.blockedDamage = blockedDamage;
        this.blockingTicks = blockingTicks;
        this.cooldownTicks = cooldownTicks;
    }

    public ShieldDefinition(float blockedDamage, int blockingTicks, int cooldownTicks) {
        this.blockedDamage = blockedDamage;
        this.blockingTicks = blockingTicks;
        this.cooldownTicks = cooldownTicks;
    }

    public static ShieldDefinition fromNetwork(FriendlyByteBuf buf) {
        return new ShieldDefinition(ResourceLocation.tryParse(buf.readUtf()), buf.readFloat(), buf.readInt(), buf.readInt());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(this.id.toString());
        buf.writeFloat(this.blockedDamage);
        buf.writeInt(this.blockingTicks);
        buf.writeInt(this.cooldownTicks);
    }

    public static void toNetwork(FriendlyByteBuf buf, ShieldDefinition shieldDefinition) {
        shieldDefinition.toNetwork(buf);
    }
}

package insane96mcp.shieldsplus.setup;

import com.mojang.serialization.Codec;
import insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SPDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ShieldsPlus.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> BLOCKED_DAMAGE =
            DATA_COMPONENTS.register("blocked_damage", () -> DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> BLOCKING_TIME =
            DATA_COMPONENTS.register("blocking_time", () -> DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> COOLDOWN =
            DATA_COMPONENTS.register("cooldown", () -> DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .build());
}

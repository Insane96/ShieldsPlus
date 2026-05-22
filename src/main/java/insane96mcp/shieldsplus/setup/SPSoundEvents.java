package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SPSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ShieldsPlus.MOD_ID);
    public static final DeferredHolder<SoundEvent, SoundEvent> CELESTIAL_GUARDIAN = SOUND_EVENTS.register("celestial_guardian", () -> SoundEvent.createFixedRangeEvent(ShieldsPlus.id("celestial_guardian"), 24f));
}

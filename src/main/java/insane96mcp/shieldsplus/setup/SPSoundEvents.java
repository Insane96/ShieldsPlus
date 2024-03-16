package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SPSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ShieldsPlus.MOD_ID);
    public static final RegistryObject<SoundEvent> CELESTIAL_GUARDIAN = SOUND_EVENTS.register("celestial_guardian", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(ShieldsPlus.MOD_ID, "celestial_guardian"), 24f));
}

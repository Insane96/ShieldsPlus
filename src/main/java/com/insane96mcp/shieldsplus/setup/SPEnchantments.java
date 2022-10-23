package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SPEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ShieldsPlus.MOD_ID);

    public static final RegistryObject<Enchantment> RECOIL = ENCHANTMENTS.register("recoil", ShieldRecoilEnchantment::new);
    public static final RegistryObject<Enchantment> REFLECTION = ENCHANTMENTS.register("reflection", ShieldReflectionEnchantment::new);
    public static final RegistryObject<Enchantment> REINFORCED = ENCHANTMENTS.register("reinforced", ShieldReinforcedEnchantment::new);
    public static final RegistryObject<Enchantment> ABLAZE = ENCHANTMENTS.register("ablaze", ShieldAblazeEnchantment::new);
    public static final RegistryObject<Enchantment> LIGHTWEIGHT = ENCHANTMENTS.register("lightweight", ShieldLightweightEnchantment::new);
    public static final RegistryObject<Enchantment> FAST_RECOVERY = ENCHANTMENTS.register("fast_recovery", ShieldFastRecoveryEnchantment::new);
    public static final RegistryObject<Enchantment> SHIELD_BASH = ENCHANTMENTS.register("shield_bash", ShieldBashEnchantment::new);
    public static final RegistryObject<Enchantment> PARRY = ENCHANTMENTS.register("parry", ShieldParryEnchantment::new);
}
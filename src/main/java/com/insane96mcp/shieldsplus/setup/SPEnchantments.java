package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldPointedEnchantment;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldRecoilEnchantment;
import com.insane96mcp.shieldsplus.world.item.enchantment.ShieldReinforcedEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SPEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ShieldsPlus.MOD_ID);

    public static final RegistryObject<Enchantment> RECOIL = ENCHANTMENTS.register("recoil", ShieldRecoilEnchantment::new);
    public static final RegistryObject<Enchantment> POINTED = ENCHANTMENTS.register("pointed", ShieldPointedEnchantment::new);
    public static final RegistryObject<Enchantment> REINFORCED = ENCHANTMENTS.register("reinforced", ShieldReinforcedEnchantment::new);
}
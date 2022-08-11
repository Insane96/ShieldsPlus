package com.insane96mcp.shieldsplus.setup;

import com.insane96mcp.shieldsplus.ShieldsPlus;
import com.insane96mcp.shieldsplus.world.item.crafting.SPShieldDecorationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SPRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ShieldsPlus.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<SPShieldDecorationRecipe>> SHIELD_DECORATION_RECIPE = RECIPE_SERIALIZERS.register("shield_decoration_recipe", () -> new SimpleRecipeSerializer<>(SPShieldDecorationRecipe::new));
}

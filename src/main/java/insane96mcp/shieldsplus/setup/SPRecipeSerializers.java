package insane96mcp.shieldsplus.setup;

import insane96mcp.shieldsplus.ShieldsPlus;
import insane96mcp.shieldsplus.world.item.crafting.SPShieldDecorationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SPRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ShieldsPlus.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SPShieldDecorationRecipe>> SHIELD_DECORATION_RECIPE = RECIPE_SERIALIZERS.register("shield_decoration_recipe", () -> new SimpleCraftingRecipeSerializer<>(SPShieldDecorationRecipe::new));
}

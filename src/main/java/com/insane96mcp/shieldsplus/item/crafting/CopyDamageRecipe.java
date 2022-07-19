package com.insane96mcp.shieldsplus.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CopyDamageRecipe extends CustomRecipe {
    public CopyDamageRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        /*List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < p_44138_.getContainerSize(); ++i) {
            ItemStack itemstack = p_44138_.getItem(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1 || !itemstack1.isRepairable()) {
                        return false;
                    }
                }
            }
        }

        return list.size() == 2;*/
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}

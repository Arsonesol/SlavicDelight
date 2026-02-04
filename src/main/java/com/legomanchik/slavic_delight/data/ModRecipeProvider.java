package com.legomanchik.slavic_delight.data;

import com.legomanchik.slavic_delight.data.recipe.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ClayPotRecipes.register(output);
        BrewBarrelRecipes.register(output);
        JarRecipes.register(output);
        SlavicCookingRecipes.register(output);
        SlavicCuttingRecipes.register(output);
        CraftingRecipes.register(output);
        SmeltingRecipes.register(output);
    }
}
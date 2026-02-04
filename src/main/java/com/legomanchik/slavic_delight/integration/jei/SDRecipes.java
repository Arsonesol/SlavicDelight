package com.legomanchik.slavic_delight.integration.jei;

import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

public class SDRecipes {
    private final RecipeManager recipeManager;

    public SDRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level != null) {
            this.recipeManager = level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft world must not be null.");
        }
    }

    public List<RecipeHolder<ClayPotRecipe>> getClayPotRecipes() {
        return recipeManager.getAllRecipesFor(ModRecipes.CLAY_POT_COOKING.get());
    }
    public List<RecipeHolder<BrewBarrelRecipe>> getBrewBarrelRecipes() {
        return recipeManager.getAllRecipesFor(ModRecipes.BREWING.get());
    }
    public List<RecipeHolder<JarRecipe>> getJarRecipes() {
        return recipeManager.getAllRecipesFor(ModRecipes.PICKLING.get());
    }
}

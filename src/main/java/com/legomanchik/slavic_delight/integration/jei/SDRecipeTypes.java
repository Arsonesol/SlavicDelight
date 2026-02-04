package com.legomanchik.slavic_delight.integration.jei;

import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SDRecipeTypes {
    public static final RecipeType<RecipeHolder<ClayPotRecipe>> CLAY_POT_COOKING = RecipeType.createFromVanilla(ModRecipes.CLAY_POT_COOKING.get());
    public static final RecipeType<RecipeHolder<BrewBarrelRecipe>> BREWING = RecipeType.createFromVanilla(ModRecipes.BREWING.get());
    public static final RecipeType<RecipeHolder<JarRecipe>> PICKLING = RecipeType.createFromVanilla(ModRecipes.PICKLING.get());
}

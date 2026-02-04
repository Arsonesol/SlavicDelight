package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import com.legomanchik.slavic_delight.data.builder.JarRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class JarRecipes {
    public static final int FAST_PICKLING = 100;      // 5 seconds
    public static final int NORMAL_PICKLING = 24000;    // 12 minutes
    public static final int SLOW_PICKLING = 400;      // 20 seconds

    public static void register(RecipeOutput output) {
        soups(output);
    }

    private static void soups(RecipeOutput output) {
        JarRecipeBuilder.jarRecipe(ModBlocks.PICKLES_JAR.get(), 1, NORMAL_PICKLING)
                .addIngredient(ModItems.CUCUMBER.get())
                .addIngredient(ModItems.CUCUMBER.get())
                .addIngredient(ModItems.CUCUMBER.get())
                .build(output);
        JarRecipeBuilder.jarRecipe(ModBlocks.SAUERKRAUT_JAR.get(), 1, NORMAL_PICKLING)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE.get())
                .addIngredient(Items.CARROT)
                .build(output);
    }
}

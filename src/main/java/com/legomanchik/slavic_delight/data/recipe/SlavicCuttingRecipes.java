package com.legomanchik.slavic_delight.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

public class SlavicCuttingRecipes {

    public static void register(RecipeOutput output) {
        cuttingAnimalItems(output);
    }

    private static void cuttingAnimalItems(RecipeOutput output) {
        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(ModItems.BACON.get()),
                        Ingredient.of(com.legomanchik.slavic_delight.common.registry.ModItems.PUSHER.get()),
                        com.legomanchik.slavic_delight.common.registry.ModItems.MINCED_PORK.get(),
                        1)
                .build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(ModItems.CHICKEN_CUTS.get()),
                        Ingredient.of(com.legomanchik.slavic_delight.common.registry.ModItems.PUSHER.get()),
                        com.legomanchik.slavic_delight.common.registry.ModItems.MINCED_CHICKEN.get(),
                        1)
                .build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(
                Ingredient.of(ModItems.COD_SLICE.get()),
                        Ingredient.of(com.legomanchik.slavic_delight.common.registry.ModItems.PUSHER.get()),
                        com.legomanchik.slavic_delight.common.registry.ModItems.MINCED_FISH.get(),
                        1)
                .build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(com.legomanchik.slavic_delight.common.registry.ModItems.BOILED_POTATOES.get()),
                        Ingredient.of(com.legomanchik.slavic_delight.common.registry.ModItems.PUSHER.get()),
                        com.legomanchik.slavic_delight.common.registry.ModItems.MASHED_POTATOES.get(),
                        1)
                .build(output);
    }
}

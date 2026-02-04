package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.client.recipebook.ClayPotRecipeBookTab;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import com.legomanchik.slavic_delight.data.builder.ClayPotRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class ClayPotRecipes {
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static void register(RecipeOutput output) {
        soups(output);
    }

    private static void soups(RecipeOutput output) {
        ClayPotRecipeBuilder.clayPotRecipe(ModBlocks.POT_POTATOES_WITH_MUSHROOMS.get(), 1, NORMAL_COOKING)
                .addIngredient(ModItems.BOILED_POTATOES.get())
                .addIngredient(ModItems.BOILED_POTATOES.get())
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .unlockedByAnyIngredient(Items.BROWN_MUSHROOM, ModItems.BOILED_POTATOES.get())
                .setRecipeBookTab(ClayPotRecipeBookTab.SOUPS)
                .build(output);
        ClayPotRecipeBuilder.clayPotRecipe(ModBlocks.POT_ROAST.get(), 1, NORMAL_COOKING)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.BEEF)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.TOMATO.get())
                .unlockedByAnyIngredient(Items.BROWN_MUSHROOM, ModItems.BOILED_POTATOES.get())
                .setRecipeBookTab(ClayPotRecipeBookTab.SOUPS)
                .build(output);
        ClayPotRecipeBuilder.clayPotRecipe(ModBlocks.POT_ROAST_WITH_CARROT.get(), 1, NORMAL_COOKING)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.PORKCHOP)
                .addIngredient(Items.CARROT)
                .unlockedByAnyIngredient(Items.BROWN_MUSHROOM, ModItems.BOILED_POTATOES.get())
                .setRecipeBookTab(ClayPotRecipeBookTab.SOUPS)
                .build(output);
    }
}

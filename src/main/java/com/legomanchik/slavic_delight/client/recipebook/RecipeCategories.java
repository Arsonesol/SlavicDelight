package com.legomanchik.slavic_delight.client.recipebook;

import com.google.common.collect.ImmutableList;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;


public class RecipeCategories
{
    public static RecipeBookCategories CLAY_POT_COOKING_SEARCH = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_CLAY_POT_COOKING_SEARCH");
    public static RecipeBookCategories CLAY_POT_COOKING_SOUPS = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_CLAY_POT_COOKING_SOUPS");
    public static RecipeBookCategories CLAY_POT_COOKING_MISC = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_CLAY_POT_COOKING_MISC");

    public static RecipeBookCategories BREWING_SEARCH = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_BREWING_SEARCH");
    public static RecipeBookCategories BREWING_DRINKS = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_BREWING_DRINKS");
    public static RecipeBookCategories BREWING_MISC = RecipeBookCategories.valueOf("SLAVIC_DELIGHT_BREWING_MISC");


    public static void init(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(RecipeBookType.valueOf("SLAVIC_DELIGHT_CLAY_POT_COOKING"), ImmutableList.of(CLAY_POT_COOKING_SEARCH, CLAY_POT_COOKING_SOUPS, CLAY_POT_COOKING_MISC));
        event.registerAggregateCategory(CLAY_POT_COOKING_SEARCH, ImmutableList.of(CLAY_POT_COOKING_SOUPS));
        event.registerRecipeCategoryFinder(ModRecipes.CLAY_POT_COOKING.get(), recipe ->
        {
            if (recipe.value() instanceof ClayPotRecipe cookingRecipe) {
                ClayPotRecipeBookTab tab = cookingRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case SOUPS -> CLAY_POT_COOKING_SOUPS;
                    };
                }
            }
            return CLAY_POT_COOKING_MISC;
        });

        event.registerBookCategories(RecipeBookType.valueOf("SLAVIC_DELIGHT_BREWING"), ImmutableList.of(BREWING_SEARCH, BREWING_DRINKS, BREWING_MISC));
        event.registerAggregateCategory(BREWING_SEARCH, ImmutableList.of(BREWING_DRINKS));
        event.registerRecipeCategoryFinder(ModRecipes.BREWING.get(), recipe ->
        {
            if (recipe.value() instanceof BrewBarrelRecipe brewingRecipe) {
                BrewBarrelRecipeBookTab tab = brewingRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {
                        case DRINKS -> BREWING_DRINKS;
                    };
                }
            }
            return BREWING_MISC;
        });
    }
}

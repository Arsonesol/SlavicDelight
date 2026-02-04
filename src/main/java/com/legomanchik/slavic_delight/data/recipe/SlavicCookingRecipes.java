package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

public class SlavicCookingRecipes {
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;     // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static void register(RecipeOutput output) {
        cookMeals(output);
        cookMisc(output);
    }

    private static void cookMeals(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BORSCH.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.POTATO)
                .addIngredient(Items.BEETROOT)
                .addIngredient(Items.CARROT)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.CABBAGE_LEAF.get())
                .addIngredient(Items.BEEF)
                .unlockedByAnyIngredient(Items.POTATO, Items.BEETROOT, Items.CARROT,
                        vectorwing.farmersdelight.common.registry.ModItems.CABBAGE_LEAF.get(), Items.BEEF)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.KURNIK.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get())
                .addIngredient(ModItems.BOILED_POTATOES.get())
                .addIngredient(ModItems.MINCED_CHICKEN.get())
                .addIngredient(ModItems.MINCED_CHICKEN.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get(), ModItems.BOILED_POTATOES.get(),
                        ModItems.MINCED_CHICKEN.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PELMENI.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get(),
                        vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SOLYANKA.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(Items.BEEF)
                .addIngredient(Items.BEEF)
                .addIngredient(Items.POTATO)
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get(),
                        Items.BEEF, Items.POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SVEKOLNIK.get(), 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
                .addIngredient(Items.BEETROOT)
                .addIngredient(ModItems.CUCUMBER.get())
                .addIngredient(ModItems.BOILED_EGG.get())
                .unlockedByAnyIngredient(Items.BEETROOT, ModItems.CUCUMBER.get(), ModItems.BOILED_EGG.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(output);
    }

    private static void cookMisc(RecipeOutput output) {
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PIROZHOK_WITH_ONIONS.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get())
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.ONION.get())
                .addIngredient(ModItems.BOILED_EGG.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get(),
                        vectorwing.farmersdelight.common.registry.ModItems.ONION.get(), ModItems.BOILED_EGG.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PIROZHOK_WITH_POTATOES.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
                .addIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get())
                .addIngredient(ModItems.MASHED_POTATOES.get())
                .unlockedByAnyIngredient(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get(),
                        ModItems.MASHED_POTATOES.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BOILED_EGG.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(Items.EGG).unlockedByAnyIngredient(Items.EGG)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BOILED_POTATOES.get(), 1, FAST_COOKING, SMALL_EXP)
                .addIngredient(Items.POTATO)
                .unlockedByAnyIngredient(Items.POTATO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(output);
        CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SAUSAGE.get(), 1, NORMAL_COOKING, SMALL_EXP)
                .addIngredient(ModItems.MINCED_PORK.get())
                .unlockedByAnyIngredient(ModItems.MINCED_PORK.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
                .build(output);
    }
}

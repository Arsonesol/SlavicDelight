package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.client.recipebook.BrewBarrelRecipeBookTab;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import com.legomanchik.slavic_delight.data.builder.BrewBarrelRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class BrewBarrelRecipes {
    public static final int FAST_BREWING = 100;      // 5 seconds
    public static final int NORMAL_BREWING = 200;    // 10 seconds
    public static final int SLOW_BREWING = 400;      // 20 seconds

    public static void register(RecipeOutput output) {
        drinks(output);
    }

    private static void drinks(RecipeOutput output) {
        BrewBarrelRecipeBuilder.brewBarrelRecipe(ModItems.KVASS_BOTTLE.get(), 1, SLOW_BREWING, 1, Items.GLASS_BOTTLE)
                .addIngredient(Items.BREAD)
                .addIngredient(Items.BREAD)
                .addIngredient(Items.BREAD)
                .addIngredient(Items.BREAD)
                .unlockedByAnyIngredient(Items.BREAD, Items.GLASS_BOTTLE)
                .setRecipeBookTab(BrewBarrelRecipeBookTab.DRINKS)
                .build(output);
        BrewBarrelRecipeBuilder.brewBarrelRecipe(ModItems.KEFIR_BOTTLE.get(), 1, SLOW_BREWING, 1, vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get())
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .unlockedByAnyIngredient(Items.BROWN_MUSHROOM, vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get())
                .setRecipeBookTab(BrewBarrelRecipeBookTab.DRINKS)
                .build(output);
    }
}

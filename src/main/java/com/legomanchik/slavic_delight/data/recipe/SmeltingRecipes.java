package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SmeltingRecipes
{
    public static void register(RecipeOutput output) {
        foodSmeltingRecipes("pork_patty", ModItems.MINCED_PORK.get(), ModItems.PORK_PATTY.get(), 0.35F, output);
        foodSmeltingRecipes("chicken_patty", ModItems.MINCED_CHICKEN.get(), ModItems.CHICKEN_PATTY.get(), 0.35F, output);

        campfireCookingRecipes("pancakes", vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get(), ModItems.PANCAKES.get(), 0.35F, output);
    }

    private static void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, RecipeOutput output) {
        String namePrefix = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, name).toString();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(output);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(output, namePrefix + "_from_campfire_cooking");
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(output, namePrefix + "_from_smoking");
    }

    private static void campfireCookingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, RecipeOutput output) {
        String namePrefix = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, name).toString();
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
                .save(output, namePrefix + "_from_campfire_cooking");
    }
}

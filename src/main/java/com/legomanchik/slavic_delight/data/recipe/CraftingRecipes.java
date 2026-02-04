package com.legomanchik.slavic_delight.data.recipe;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class CraftingRecipes {
    public static void register(RecipeOutput output) {
        recipesTools(output);
        recipesBlocks(output);
        recipesItems(output);
        recipesFunctionalBlocks(output);
        recipesFoodStaff(output);
        recipesCraftedMeals(output);
    }

    private static void recipesTools(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PUSHER.get())
                .pattern("#")
                .pattern("s")
                .define('#', Blocks.STRIPPED_OAK_LOG)
                .define('s', Items.STICK)
                .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(output);
    }

    private static void recipesFunctionalBlocks(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BREW_BARREL.get())
                .pattern("sss")
                .pattern("ibi")
                .pattern("psp")
                .define('s', Blocks.OAK_SLAB)
                .define('p', Blocks.OAK_PLANKS)
                .define('b', Items.BUCKET)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CLAY_POT.get())
                .pattern("b b")
                .pattern("bbb")
                .define('b', Items.BRICK)
                .unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.JAR.get())
                .pattern("gsg")
                .pattern("g g")
                .pattern("ggg")
                .define('g', Blocks.GLASS)
                .define('s', Blocks.OAK_SLAB)
                .unlockedBy("has_glass", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLASS))
                .save(output);
    }

    private static void recipesCraftedMeals(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CRAB_SALAD.get())
                .requires(CommonTags.CROPS_ONION)
                .requires(ModItems.CRAB_STICKS.get())
                .requires(ModItems.BOILED_EGG.get())
                .requires(Items.BOWL)
                .unlockedBy("has_crab_sticks", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRAB_STICKS.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MASH_AND_PATTY.get())
                .requires(ModItems.PORK_PATTY.get())
                .requires(ModItems.MASHED_POTATOES.get())
                .requires(Items.BOWL)
                .unlockedBy("has_mashed_potatoes", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MASHED_POTATOES.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.OKROSHKA_KEFIR.get())
                .requires(ModItems.RADISH.get())
                .requires(ModItems.CUCUMBER.get())
                .requires(ModItems.BOILED_EGG.get())
                .requires(ModItems.BOILED_POTATOES.get())
                .requires(ModItems.KEFIR_BOTTLE.get())
                .requires(ModItems.SAUSAGE.get())
                .requires(Items.BOWL)
                .unlockedBy("has_kefir_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KEFIR_BOTTLE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.OKROSHKA_KVASS.get())
                .requires(ModItems.RADISH.get())
                .requires(ModItems.CUCUMBER.get())
                .requires(ModItems.BOILED_EGG.get())
                .requires(ModItems.BOILED_POTATOES.get())
                .requires(ModItems.KVASS_BOTTLE.get())
                .requires(ModItems.SAUSAGE.get())
                .requires(Items.BOWL)
                .unlockedBy("has_kvass_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KVASS_BOTTLE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.PANCAKES_WITH_BERRIES.get())
                .requires(ModItems.PANCAKES.get())
                .requires(Items.SWEET_BERRIES)
                .unlockedBy("has_pancakes", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PANCAKES.get()))
                .save(output);
    }

    private static void recipesFoodStaff(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CRAB_STICKS.get())
                .requires(ModItems.MINCED_FISH.get())
                .requires(Items.RED_DYE)
                .unlockedBy("has_minced_fish", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MINCED_FISH.get()))
                .save(output);
    }

    private static void recipesBlocks(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUCUMBER_CRATE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.CUCUMBER.get())
                .unlockedBy("has_cucumber", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CUCUMBER.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RADISH_CRATE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.RADISH.get())
                .unlockedBy("has_radish", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RADISH.get()))
                .save(output);
    }

    private static void recipesItems(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CUCUMBER.get(), 9)
                .requires(ModBlocks.CUCUMBER_CRATE.get())
                .unlockedBy("has_cucumber_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CUCUMBER_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RADISH.get(), 9)
                .requires(ModBlocks.RADISH_CRATE.get())
                .unlockedBy("has_radish_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.RADISH_CRATE.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CUCUMBER_SEEDS.get())
                .requires(ModItems.CUCUMBER.get())
                .unlockedBy("has_cucumber", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CUCUMBER.get()))
                .save(output);
        //ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CUCUMBER_SEEDS.get())
        //        .requires(ModBlocks.WILD_CUCUMBERS.get())
        //        .unlockedBy("has_cucumber", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.WILD_CUCUMBERS.get()))
        //        .save(output);
    }
}

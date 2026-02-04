package com.legomanchik.slavic_delight.data.builder;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.client.recipebook.BrewBarrelRecipeBookTab;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedHashMap;
import java.util.Map;

public class BrewBarrelRecipeBuilder implements RecipeBuilder
{
    private BrewBarrelRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Item result;
    private final ItemStack resultStack;
    private final int brewingTime;
    private final float experience;
    private final ItemStack bottle;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public BrewBarrelRecipeBuilder(ItemLike result, int count, int brewingTime, float experience, ItemLike bottle) {
        this(new ItemStack(result, count), brewingTime, experience, bottle);
    }

    public BrewBarrelRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, ItemLike bottle) {
        this.result = resultIn.getItem();
        this.resultStack = resultIn;
        this.brewingTime = cookingTime;
        this.experience = experience;
        this.bottle = bottle != null ? new ItemStack(bottle) : ItemStack.EMPTY;
        this.tab = null;
    }

    public static BrewBarrelRecipeBuilder brewBarrelRecipe(ItemLike mainResult, int count, int brewingTime, float experience) {
        return new BrewBarrelRecipeBuilder(mainResult, count, brewingTime, experience, null);
    }

    public static BrewBarrelRecipeBuilder brewBarrelRecipe(ItemLike mainResult, int count, int brewingTime, float experience, ItemLike container) {
        return new BrewBarrelRecipeBuilder(mainResult, count, brewingTime, experience, container);
    }

    public BrewBarrelRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(tagIn));
    }

    public BrewBarrelRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public BrewBarrelRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public BrewBarrelRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public BrewBarrelRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    @Override
    public RecipeBuilder group(String string) {
        return this;
    }

    public BrewBarrelRecipeBuilder setRecipeBookTab(BrewBarrelRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public BrewBarrelRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
        this.criteria.put(criterionName, criterionTrigger);
        return this;
    }

    public BrewBarrelRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    public BrewBarrelRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
        return this;
    }

    public void build(RecipeOutput output) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
        save(output, ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, location.getPath()));
    }

    public void build(RecipeOutput outputIn, String save) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(result);
        if ((ResourceLocation.parse(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Brewing Recipe " + save + " should remove its 'save' argument");
        } else {
            save(outputIn, ResourceLocation.parse(save));
        }
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation recipeId = id.withPrefix("brewing/");
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        BrewBarrelRecipe recipe = new BrewBarrelRecipe(
                "",
                this.tab,
                this.ingredients,
                this.resultStack,
                this.bottle,
                this.experience,
                this.brewingTime
        );
        output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/brewing/")));
    }
}

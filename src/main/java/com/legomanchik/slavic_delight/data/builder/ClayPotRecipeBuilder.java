package com.legomanchik.slavic_delight.data.builder;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.client.recipebook.ClayPotRecipeBookTab;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
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

public class ClayPotRecipeBuilder implements RecipeBuilder
{
    private ClayPotRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Item result;
    private final ItemStack resultStack;
    private final int cookingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public ClayPotRecipeBuilder(ItemLike result, int count, int cookingTime) {
        this(new ItemStack(result, count), cookingTime);
    }

    public ClayPotRecipeBuilder(ItemStack resultIn, int cookingTime) {
        this.result = resultIn.getItem();
        this.resultStack = resultIn;
        this.cookingTime = cookingTime;
        this.tab = null;
    }

    public static ClayPotRecipeBuilder clayPotRecipe(ItemLike mainResult, int count, int cookingTime) {
        return new ClayPotRecipeBuilder(mainResult, count, cookingTime);
    }

    public ClayPotRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(tagIn));
    }

    public ClayPotRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public ClayPotRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public ClayPotRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public ClayPotRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    @Override
    public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
        return this;
    }

    public ClayPotRecipeBuilder setRecipeBookTab(ClayPotRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public ClayPotRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
        this.criteria.put(criterionName, criterionTrigger);
        return this;
    }

    public ClayPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    public ClayPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
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
            throw new IllegalStateException("Clay Pot Recipe " + save + " should remove its 'save' argument");
        } else {
            save(outputIn, ResourceLocation.parse(save));
        }
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation recipeId = id.withPrefix("clay_pot_cooking/");
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        ClayPotRecipe recipe = new ClayPotRecipe(
                "",
                this.tab,
                this.ingredients,
                this.resultStack,
                this.cookingTime
        );
        output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/clay_pot_cooking/")));
    }
}

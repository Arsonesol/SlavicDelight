package com.legomanchik.slavic_delight.data.builder;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
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

public class JarRecipeBuilder implements RecipeBuilder
{
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Item result;
    private final ItemStack resultStack;
    private final int picklingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public JarRecipeBuilder(ItemLike result, int count, int cookingTime) {
        this(new ItemStack(result, count), cookingTime);
    }

    public JarRecipeBuilder(ItemStack resultIn, int cookingTime) {
        this.result = resultIn.getItem();
        this.resultStack = resultIn;
        this.picklingTime = cookingTime;
    }

    public static JarRecipeBuilder jarRecipe(ItemLike mainResult, int count, int cookingTime) {
        return new JarRecipeBuilder(mainResult, count, cookingTime);
    }

    public JarRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return addIngredient(Ingredient.of(tagIn));
    }

    public JarRecipeBuilder addIngredient(ItemLike itemIn) {
        return addIngredient(itemIn, 1);
    }

    public JarRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            addIngredient(Ingredient.of(itemIn));
        }
        return this;
    }

    public JarRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return addIngredient(ingredientIn, 1);
    }

    public JarRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            ingredients.add(ingredientIn);
        }
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        return null;
    }

    @Override
    public RecipeBuilder group(String p_176495_) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    public void build(RecipeOutput output) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
        save(output, ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, location.getPath()));
    }

    public void build(RecipeOutput outputIn, String save) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(result);
        if ((ResourceLocation.parse(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Jar Recipe " + save + " should remove its 'save' argument");
        } else {
            save(outputIn, ResourceLocation.parse(save));
        }
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ResourceLocation recipeId = id.withPrefix("jar/");
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancementBuilder::addCriterion);
        JarRecipe recipe = new JarRecipe(
                "",
                this.ingredients,
                this.resultStack,
                this.picklingTime
        );
        output.accept(recipeId, recipe, advancementBuilder.build(id.withPrefix("recipes/jar/")));
    }
}

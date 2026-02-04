package com.legomanchik.slavic_delight.common.crafting;

import com.legomanchik.slavic_delight.client.recipebook.BrewBarrelRecipeBookTab;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModRecipeSerializers;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

public class BrewBarrelRecipe implements Recipe<RecipeWrapper>
{
    public static final int INPUT_SLOTS = 4;
    public static final int BOTTLE_SLOT = 4;

    private final String group;
    private final BrewBarrelRecipeBookTab tab;
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack bottle;
    private final ItemStack containerOverride;
    private final float experience;
    private final int brewingTime;

    public BrewBarrelRecipe(String group, BrewBarrelRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack bottle, float experience, int brewingTime) {
        this.group = group;
        this.tab = tab;
        this.inputItems = inputItems;
        this.output = output;

        if (!bottle.isEmpty()) {
            this.bottle = bottle;
        } else if (!output.getCraftingRemainingItem().isEmpty()) {
            this.bottle = output.getCraftingRemainingItem();
        } else {
            this.bottle = ItemStack.EMPTY;
        }

        this.containerOverride = bottle;
        this.experience = experience;
        this.brewingTime = brewingTime;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public BrewBarrelRecipeBookTab getRecipeBookTab() {
        return this.tab;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputItems;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output;
    }

    public ItemStack getBottle() {
        return this.bottle;
    }

    public ItemStack getContainerOverride() {
        return this.containerOverride;
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv, HolderLookup.Provider provider) {
        return this.output.copy();
    }

    public float getExperience() {
        return this.experience;
    }

    public int getBrewingTime() {
        return this.brewingTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < INPUT_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        boolean ingredientsMatch = i == this.inputItems.size() &&
                RecipeMatcher.findMatches(inputs, this.inputItems) != null;
        if (!ingredientsMatch) {
            return false;
        }

        ItemStack bottleInSlot = inv.getItem(BOTTLE_SLOT);
        if (!this.bottle.isEmpty()) {

            return !bottleInSlot.isEmpty() &&
                    ItemStack.isSameItemSameComponents(bottleInSlot, this.bottle) &&
                    bottleInSlot.getCount() >= this.bottle.getCount();
        } else {
            return bottleInSlot.isEmpty();
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.inputItems.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BREWING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BREWING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.BREW_BARREL.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrewBarrelRecipe that = (BrewBarrelRecipe) o;

        if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
        if (getBrewingTime() != that.getBrewingTime()) return false;
        if (!getGroup().equals(that.getGroup())) return false;
        if (tab != that.tab) return false;
        if (!inputItems.equals(that.inputItems)) return false;
        if (!output.equals(that.output)) return false;
        return bottle.equals(that.bottle);
    }

    @Override
    public int hashCode() {
        int result = getGroup().hashCode();
        result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
        result = 31 * result + inputItems.hashCode();
        result = 31 * result + output.hashCode();
        result = 31 * result + bottle.hashCode();
        result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
        result = 31 * result + getBrewingTime();
        return result;
    }

    public static class Serializer implements RecipeSerializer<BrewBarrelRecipe>
    {
        private static final MapCodec<BrewBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(BrewBarrelRecipe::getGroup),
                BrewBarrelRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", BrewBarrelRecipeBookTab.DRINKS).forGetter(BrewBarrelRecipe::getRecipeBookTab),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
                    NonNullList<Ingredient> nonNullList = NonNullList.create();
                    nonNullList.addAll(ingredients);
                    return nonNullList;
                }, ingredients -> ingredients).forGetter(BrewBarrelRecipe::getIngredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.output),
                ItemStack.STRICT_CODEC.optionalFieldOf("bottle", ItemStack.EMPTY).forGetter(BrewBarrelRecipe::getContainerOverride),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(BrewBarrelRecipe::getExperience),
                Codec.INT.optionalFieldOf("brewingTime", 200).forGetter(BrewBarrelRecipe::getBrewingTime)
        ).apply(inst, BrewBarrelRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf,BrewBarrelRecipe> STREAM_CODEC = StreamCodec.of(BrewBarrelRecipe.Serializer::toNetwork, BrewBarrelRecipe.Serializer::fromNetwork);

        public Serializer() {
        }

        @Override
        public MapCodec<BrewBarrelRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BrewBarrelRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static BrewBarrelRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String groupIn = buffer.readUtf();
            BrewBarrelRecipeBookTab tabIn = BrewBarrelRecipeBookTab.findByName(buffer.readUtf());
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            inputItemsIn.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            ItemStack outputIn = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            float experienceIn = buffer.readFloat();
            int cookTimeIn = buffer.readVarInt();
            return new BrewBarrelRecipe(groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BrewBarrelRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            buffer.writeVarInt(recipe.inputItems.size());

            for (Ingredient ingredient : recipe.inputItems) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.bottle);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.brewingTime);
        }
    }
}

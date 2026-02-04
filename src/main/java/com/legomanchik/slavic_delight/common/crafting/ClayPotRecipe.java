package com.legomanchik.slavic_delight.common.crafting;

import com.legomanchik.slavic_delight.client.recipebook.ClayPotRecipeBookTab;
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

public class ClayPotRecipe implements Recipe<RecipeWrapper> {
    public static final int INPUT_SLOTS = 4;

    private final String group;
    private final ClayPotRecipeBookTab tab;
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final int cookTime;

    public ClayPotRecipe(String group, ClayPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, int cookTime) {
        this.group = group;
        this.tab = tab;
        this.inputItems = inputItems;
        this.output = output;
        this.cookTime = cookTime;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public ClayPotRecipeBookTab getRecipeBookTab() {
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

    @Override
    public ItemStack assemble(RecipeWrapper inv, HolderLookup.Provider provider) {
        return this.output.copy();
    }

    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < INPUT_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.inputItems.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CLAY_POT_COOKING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CLAY_POT_COOKING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.CLAY_POT.get());
    }

    @Override
    public int hashCode() {
        int result = getGroup().hashCode();
        result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
        result = 31 * result + inputItems.hashCode();
        result = 31 * result + output.hashCode();
        result = 31 * result + getCookTime();
        return result;
    }

    public static class Serializer implements RecipeSerializer<ClayPotRecipe>
    {
        private static final MapCodec<ClayPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ClayPotRecipe::getGroup),
                ClayPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", ClayPotRecipeBookTab.SOUPS).forGetter(ClayPotRecipe::getRecipeBookTab),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
                    NonNullList<Ingredient> nonNullList = NonNullList.create();
                    nonNullList.addAll(ingredients);
                    return nonNullList;
                }, ingredients -> ingredients).forGetter(ClayPotRecipe::getIngredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.output),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(ClayPotRecipe::getCookTime)
        ).apply(inst, ClayPotRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ClayPotRecipe> STREAM_CODEC = StreamCodec.of(ClayPotRecipe.Serializer::toNetwork, ClayPotRecipe.Serializer::fromNetwork);

        public Serializer() {
        }

        @Override
        public MapCodec<ClayPotRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ClayPotRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ClayPotRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String groupIn = buffer.readUtf();
            ClayPotRecipeBookTab tabIn = ClayPotRecipeBookTab.findByName(buffer.readUtf());
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            inputItemsIn.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            ItemStack outputIn = ItemStack.STREAM_CODEC.decode(buffer);
            int cookTimeIn = buffer.readVarInt();
            return new ClayPotRecipe(groupIn, tabIn, inputItemsIn, outputIn, cookTimeIn);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ClayPotRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            buffer.writeVarInt(recipe.inputItems.size());

            for (Ingredient ingredient : recipe.inputItems) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}

package com.legomanchik.slavic_delight.integration.jei.category;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.integration.jei.SDRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.Arrays;

public class ClayPotRecipeCategory implements IRecipeCategory<RecipeHolder<ClayPotRecipe>> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public ClayPotRecipeCategory(IGuiHelper helper) {
        title = Component.translatable("jei.slavic_delight.clay_pot_cooking");
        ResourceLocation backgroundImage = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "textures/gui/jei/clay_pot.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 82, 60);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CLAY_POT.get()));
    }

    @Override
    public RecipeType<RecipeHolder<ClayPotRecipe>> getRecipeType() {
        return SDRecipeTypes.CLAY_POT_COOKING;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ClayPotRecipe> holder, IFocusGroup focusGroup) {
        ClayPotRecipe recipe = holder.value();
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        ItemStack resultStack = RecipeUtils.getResultItem(recipe);

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                int inputIndex = row * 2 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, (column * borderSlotSize) + 4, (row * borderSlotSize) + 4)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62, 13).addItemStack(resultStack);
    }
}

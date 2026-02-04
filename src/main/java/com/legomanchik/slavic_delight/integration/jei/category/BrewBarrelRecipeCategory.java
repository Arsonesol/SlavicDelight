package com.legomanchik.slavic_delight.integration.jei.category;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.integration.jei.SDRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.Arrays;

public class BrewBarrelRecipeCategory implements IRecipeCategory<RecipeHolder<BrewBarrelRecipe>> {
    protected final IDrawableAnimated arrow;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public BrewBarrelRecipeCategory(IGuiHelper helper) {
        title = Component.translatable("jei.slavic_delight.brew_barrel_brewing");
        ResourceLocation backgroundImage = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "textures/gui/brew_barrel.png");
        background = helper.createDrawable(backgroundImage, 4, 4, 132, 76);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BREW_BARREL.get()));
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<RecipeHolder<BrewBarrelRecipe>> getRecipeType() {
        return SDRecipeTypes.BREWING;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BrewBarrelRecipe> holder, IFocusGroup focusGroup) {
        BrewBarrelRecipe recipe = holder.value();
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        ItemStack resultStack = RecipeUtils.getResultItem(recipe);
        ItemStack bottleStack = recipe.getBottle();

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                int inputIndex = row * 2 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, (column * borderSlotSize) + 22, (row * borderSlotSize) + 19)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 69, 49).addItemStack(bottleStack);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 28).addItemStack(resultStack);
    }

    @Override
    public void draw(RecipeHolder<BrewBarrelRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }
}

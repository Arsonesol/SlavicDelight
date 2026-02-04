package com.legomanchik.slavic_delight.integration.jei.category;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.integration.jei.SDRecipeTypes;
import cpw.mods.jarhandling.impl.Jar;
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

public class JarRecipeCategory implements IRecipeCategory<RecipeHolder<JarRecipe>> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public JarRecipeCategory(IGuiHelper helper) {
        title = Component.translatable("jei.slavic_delight.pickling");
        ResourceLocation backgroundImage = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "textures/gui/jei/jar.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 78, 72);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.JAR.get()));
    }

    @Override
    public RecipeType<RecipeHolder<JarRecipe>> getRecipeType() {
        return SDRecipeTypes.PICKLING;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<JarRecipe> holder, IFocusGroup focusGroup) {
        JarRecipe recipe = holder.value();
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
        ItemStack resultStack = RecipeUtils.getResultItem(recipe);

        int borderSlotSize = 18;
        for (int row = 0; row < 1; ++row) {
            for (int column = 0; column < 3; ++column) {
                if (column < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, borderSlotSize - 7, (column * borderSlotSize) + 13)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(column).getItems()));
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 31).addItemStack(resultStack);
    }

    @Override
    public void draw(RecipeHolder<JarRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }
}

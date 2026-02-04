package com.legomanchik.slavic_delight.client.gui;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class ClayPotRecipeBookComponent extends RecipeBookComponent {

    public void hide() {
        this.setVisible(false);
    }

    @Override
    protected Component getRecipeFilterName() {
        return TextUtils.getTranslation("container.recipe_book.cookable");
    }

    @Override
    public void setupGhostRecipe(RecipeHolder<?> recipe, List<Slot> slots) {
        ItemStack resultStack = recipe.value().getResultItem(this.minecraft.level.registryAccess());
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(6).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(resultStack), (slots.get(6)).x, (slots.get(6)).y);
        }

        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.value().getIngredients().iterator(), 0);
    }
}

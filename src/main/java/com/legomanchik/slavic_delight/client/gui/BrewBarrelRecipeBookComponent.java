package com.legomanchik.slavic_delight.client.gui;

import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class BrewBarrelRecipeBookComponent extends RecipeBookComponent {

    private static final int INGREDIENT_SLOT_START = 0;
    private static final int INGREDIENT_SLOT_COUNT = 4; // 0-3
    private static final int BOTTLE_SLOT_INDEX = 4;
    private static final int OUTPUT_SLOT_INDEX = 5; // 5 - резерв, 6 - результат

    public void hide() {
        this.setVisible(false);
    }

    @Override
    protected Component getRecipeFilterName() {
        return TextUtils.getTranslation("container.recipe_book.cookable");
    }

    @Override
    public void setupGhostRecipe(RecipeHolder<?> recipe, List<Slot> slots) {
        if (!(recipe.value() instanceof BrewBarrelRecipe brewRecipe)) {
            return;
        }
        this.ghostRecipe.setRecipe(recipe);

        Slot outputSlot = slots.get(OUTPUT_SLOT_INDEX);
        Slot bottleSlot = slots.get(BOTTLE_SLOT_INDEX);

        ItemStack resultStack = brewRecipe.getResultItem(this.minecraft.level.registryAccess());
        if (outputSlot.getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(resultStack),
                    outputSlot.x, outputSlot.y);
        }
        ItemStack bottleRequired = brewRecipe.getBottle();
        if (!bottleRequired.isEmpty() && bottleSlot.getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(bottleRequired),
                    bottleSlot.x, bottleSlot.y);
        }

        setupIngredientsGhost(brewRecipe, slots);
    }

    private void setupIngredientsGhost(BrewBarrelRecipe recipe, List<Slot> slots) {
        List<Ingredient> ingredients = recipe.getIngredients();

        for (int i = 0; i < Math.min(ingredients.size(), INGREDIENT_SLOT_COUNT); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                Slot slot = slots.get(INGREDIENT_SLOT_START + i);
                if (slot.getItem().isEmpty()) {
                    ItemStack[] matchingItems = ingredient.getItems();
                    if (matchingItems.length > 0) {
                        this.ghostRecipe.addIngredient(ingredient,
                                slot.x, slot.y);
                    }
                }
            }
        }
    }
}

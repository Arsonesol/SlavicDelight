package com.legomanchik.slavic_delight.integration.jei;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.client.gui.BrewBarrelScreen;
import com.legomanchik.slavic_delight.client.gui.ClayPotScreen;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.BrewBarrelMenu;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.ClayPotMenu;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModMenuTypes;
import com.legomanchik.slavic_delight.integration.jei.category.BrewBarrelRecipeCategory;
import com.legomanchik.slavic_delight.integration.jei.category.ClayPotRecipeCategory;
import com.legomanchik.slavic_delight.integration.jei.category.JarRecipeCategory;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ClayPotRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new BrewBarrelRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new JarRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        SDRecipes modRecipes = new SDRecipes();
        registration.addRecipes(SDRecipeTypes.CLAY_POT_COOKING, modRecipes.getClayPotRecipes());
        registration.addRecipes(SDRecipeTypes.BREWING, modRecipes.getBrewBarrelRecipes());
        registration.addRecipes(SDRecipeTypes.PICKLING, modRecipes.getJarRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CLAY_POT.get()), SDRecipeTypes.CLAY_POT_COOKING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BREW_BARREL.get()), SDRecipeTypes.BREWING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.JAR.get()), SDRecipeTypes.PICKLING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ClayPotScreen.class, 89, 25, 24, 17, SDRecipeTypes.CLAY_POT_COOKING);
        registration.addRecipeClickArea(BrewBarrelScreen.class, 89, 25, 24, 17, SDRecipeTypes.BREWING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ClayPotMenu.class, ModMenuTypes.CLAY_POT.get(), SDRecipeTypes.CLAY_POT_COOKING, 0, 4, 9, 36);
        registration.addRecipeTransferHandler(BrewBarrelMenu.class, ModMenuTypes.BREW_BARREL_MENU.get(), SDRecipeTypes.BREWING, 0, 4, 9, 36);

    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}

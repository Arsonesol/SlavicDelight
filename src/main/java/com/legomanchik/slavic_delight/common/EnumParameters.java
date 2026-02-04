package com.legomanchik.slavic_delight.common;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class EnumParameters
{
    public static final EnumProxy<RecipeBookCategories> PROXY_CLAY_POT_COOKING_SEARCH = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_CLAY_POT_COOKING_SOUPS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModBlocks.POT_ROAST.get()))
    );

    public static final EnumProxy<RecipeBookCategories> PROXY_CLAY_POT_COOKING_MISC = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.BOILED_POTATOES.get()))
    );

    public static final EnumProxy<RecipeBookCategories> PROXY_BREWING_SEARCH = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
    );
    public static final EnumProxy<RecipeBookCategories> PROXY_BREWING_DRINKS = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.KVASS_BOTTLE.get()))
    );

    public static final EnumProxy<RecipeBookCategories> PROXY_BREWING_MISC = new EnumProxy<>(
            RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.KVASS_BOTTLE.get()))
    );
}

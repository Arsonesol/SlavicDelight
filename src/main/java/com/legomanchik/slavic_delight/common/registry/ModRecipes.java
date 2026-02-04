package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }

    public static final Supplier<RecipeType<ClayPotRecipe>> CLAY_POT_COOKING = RECIPE_TYPES.register("clay_pot_cooking", () -> registerRecipeType("clay_pot_cooking"));
    public static final Supplier<RecipeType<BrewBarrelRecipe>> BREWING = RECIPE_TYPES.register("brewing", () -> registerRecipeType("brewing"));
    public static final Supplier<RecipeType<JarRecipe>> PICKLING = RECIPE_TYPES.register("pickling", () -> registerRecipeType("pickling"));


    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return SlavicDelight.MOD_ID + ":" + identifier;
            }
        };
    }
}

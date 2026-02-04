package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final Supplier<RecipeSerializer<?>> CLAY_POT_COOKING;
    public static final Supplier<RecipeSerializer<?>> BREWING;
    public static final Supplier<RecipeSerializer<?>> PICKLING;


    public ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }

    static {
        RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SlavicDelight.MOD_ID);
        CLAY_POT_COOKING = RECIPE_SERIALIZERS.register("clay_pot_cooking", ClayPotRecipe.Serializer::new);
        BREWING = RECIPE_SERIALIZERS.register("brewing", BrewBarrelRecipe.Serializer::new);
        PICKLING = RECIPE_SERIALIZERS.register("pickling", JarRecipe.Serializer::new);
    }
}

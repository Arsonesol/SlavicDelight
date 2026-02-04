package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;

import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.FoodDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.JarDataComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

    public static final Supplier<DataComponentType<ClayPotDataComponent>> CLAY_POT_USAGE_COUNT =
            DATA_COMPONENT_TYPES.register("clay_pot_usage_count",
                    () -> DataComponentType.<ClayPotDataComponent>builder()
                            .persistent(ClayPotDataComponent.CODEC)
                            .build());

    public static final Supplier<DataComponentType<FoodDataComponent>> FOOD_DATA =
            DATA_COMPONENT_TYPES.register("food_data",
                    () -> DataComponentType.<FoodDataComponent>builder()
                            .persistent(FoodDataComponent.CODEC)
                            .build());

    public static final Supplier<DataComponentType<JarDataComponent>> JAR_DATA =
            DATA_COMPONENT_TYPES.register("jar_data",
                    () -> DataComponentType.<JarDataComponent>builder()
                            .persistent(JarDataComponent.CODEC)
                            .build());
}

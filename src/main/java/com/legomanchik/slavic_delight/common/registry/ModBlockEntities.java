package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


@SuppressWarnings("ALL")
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final Supplier<BlockEntityType<BrewBarrelEntity>> BREW_BARREL_ENTITY =
            BLOCK_ENTITIES.register("brew_barrel_entity",
                    ()-> BlockEntityType.Builder.of(BrewBarrelEntity::new, ModBlocks.BREW_BARREL.get()).build(null));

    public static final Supplier<BlockEntityType<ClayPotBlockEntity>> CLAY_POT = BLOCK_ENTITIES.register("clay_pot",
            () -> BlockEntityType.Builder.of(ClayPotBlockEntity::new, ModBlocks.CLAY_POT.get()).build(null));

    public static final Supplier<BlockEntityType<PotWithEatBlockEntity>> POT_WITH_EAT_ENTITY =
            BLOCK_ENTITIES.register("pot_with_eat_entity",
                    () -> BlockEntityType.Builder.of(
                            PotWithEatBlockEntity::new,
                            ModBlocks.POT_POTATOES_WITH_MUSHROOMS.get(),
                            ModBlocks.POT_ROAST.get(),
                            ModBlocks.POT_ROAST_WITH_CARROT.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<JarBlockEntity>> JAR_ENTITY =
            BLOCK_ENTITIES.register("jar_entity",
                    ()-> BlockEntityType.Builder.of(JarBlockEntity::new, ModBlocks.JAR.get()).build(null));

    public static final Supplier<BlockEntityType<PicklesJarEntity>> PICKLES_JAR_ENTITY =
            BLOCK_ENTITIES.register("pickles_entity",
                    ()-> BlockEntityType.Builder.of(PicklesJarEntity::new, ModBlocks.PICKLES_JAR.get()).build(null));

    public static final Supplier<BlockEntityType<BrineJarEntity>> BRINE_JAR_ENTITY =
            BLOCK_ENTITIES.register("brine_entity",
                    ()-> BlockEntityType.Builder.of(BrineJarEntity::new, ModBlocks.BRINE_JAR.get()).build(null));
}

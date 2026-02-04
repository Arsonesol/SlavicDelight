package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.block.custom.*;
import com.legomanchik.slavic_delight.common.datacomponents.FoodDataComponent;
import com.legomanchik.slavic_delight.common.item.BlockFoodItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.WildCropBlock;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final Supplier<Block> BREW_BARREL = registerBlock("brew_barrel",
            ()-> new BrewBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)
                    .noOcclusion()));

    public static final Supplier<Block> JAR = registerBlock("jar",
            ()-> new JarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .noOcclusion()));

    public static final Supplier<Block> PICKLES_JAR = registerBlock("pickles_jar",
            ()-> new PicklesJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .noOcclusion()));

    public static final Supplier<Block> BRINE_JAR = registerBlock("brine_jar",
            ()-> new BrineJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .noOcclusion()));

    public static final Supplier<Block> SAUERKRAUT_JAR = registerBlock("sauerkraut_jar",
            ()-> new SauerkrautJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)
                    .noOcclusion()));

    public static final Supplier<Block> CLAY_POT = registerBlock("clay_pot",
            ()-> new ClayPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DECORATED_POT)
                    .noOcclusion()));

    public static final Supplier<PotWithEatBlock> POT_POTATOES_WITH_MUSHROOMS = registerEdiblePotBlock("pot_potatoes_with_mushrooms",
            ()-> new PotWithEatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DECORATED_POT)
                    .noOcclusion(), foodData(7, 0.9F, new MobEffectInstance(ModEffects.COMFORT, 3600))),
            new Item.Properties().durability(3));

    public static final Supplier<PotWithEatBlock> POT_ROAST = registerEdiblePotBlock("pot_roast",
            ()-> new PotWithEatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DECORATED_POT)
                    .noOcclusion(), foodData(10, 0.9F, new MobEffectInstance(ModEffects.COMFORT, 3600))),
            new Item.Properties().durability(3));

    public static final Supplier<PotWithEatBlock> POT_ROAST_WITH_CARROT = registerEdiblePotBlock("pot_roast_with_carrot",
            ()-> new PotWithEatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DECORATED_POT).noOcclusion(),
                    foodData(10, 0.9F, new MobEffectInstance(ModEffects.COMFORT, 3600))),
            new Item.Properties().durability(3));

    public static final Supplier<Block> CUCUMBER_CROP = BLOCKS.register("cucumber_crop",
            ()-> new CucumberCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                    .noOcclusion()
                    .noCollission()));

    public static final Supplier<Block> RADISH_CROP = BLOCKS.register("radish_crop",
            ()-> new RadishCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                    .noOcclusion()
                    .noCollission()));

    public static final Supplier<Block> WILD_CUCUMBERS = registerBlock("wild_cucumbers",
            ()-> new WildCropBlock(MobEffects.POISON, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)
                    .noCollission()
                    .noOcclusion()));

    public static final Supplier<Block> WILD_RADISH = registerBlock("wild_radish",
            ()-> new WildCropBlock(MobEffects.POISON, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)
                    .noCollission()
                    .noOcclusion()));

    public static final Supplier<Block> CUCUMBER_CRATE = registerBlock("cucumber_crate",
            ()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN)));

    public static final Supplier<Block> RADISH_CRATE = registerBlock("radish_crate",
            ()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN)));

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, Supplier<T> block) {
        ModItems.registerWithTab(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> Supplier<T> registerBlockWithCustomItem(String name, Supplier<T> block, Item.Properties properties) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerCustomBlockItem(name, toReturn, properties);
        return toReturn;
    }

    private static <T extends Block> void registerCustomBlockItem(String name, Supplier<T> block, Item.Properties properties) {
        ModItems.registerWithTab(name, () -> new BlockItem(block.get(), properties));
    }

    private static <T extends PotWithEatBlock> Supplier<T> registerEdiblePotBlock(
            String name,
            Supplier<T> blockSupplier,
            Item.Properties itemProperties) {

        Supplier<T> block = BLOCKS.register(name, blockSupplier);

        ModItems.registerWithTab(name, () -> {
            T blockInstance = block.get();
            FoodDataComponent foodData = blockInstance.getFoodData();

            return new BlockFoodItem(
                    blockInstance,
                    itemProperties,
                    foodData
            );
        });

        return block;
    }

    private static FoodDataComponent foodData(int nutrition, float saturation, MobEffectInstance... effects) {
        return new FoodDataComponent(
                nutrition,
                saturation,
                effects.length > 0 ? List.of(effects) : List.of()
        );
    }
}

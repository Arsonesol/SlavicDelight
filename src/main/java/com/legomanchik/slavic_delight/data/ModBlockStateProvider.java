package com.legomanchik.slavic_delight.data;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.block.custom.BrewBarrelBlock;
import com.legomanchik.slavic_delight.common.block.custom.CucumberCropBlock;
import com.legomanchik.slavic_delight.common.block.custom.RadishCropBlock;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider
{
    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SlavicDelight.MOD_ID, existingFileHelper);
    }

    private String blockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "block/" + path);
    }

    public ModelFile existingModel(Block block) {
        return new ModelFile.ExistingModelFile(resourceBlock(blockName(block)), models().existingFileHelper);
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(resourceBlock(path), models().existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.crateBlock(ModBlocks.RADISH_CRATE.get(), "radish");
        this.crateBlock(ModBlocks.CUCUMBER_CRATE.get(), "cucumber");

        this.customStageBlock(ModBlocks.RADISH_CROP.get(), resourceBlock("crop_cross"), "cross", RadishCropBlock.AGE, new ArrayList<>());
        this.customStageBlock(ModBlocks.CUCUMBER_CROP.get(), resourceBlock("crop_cross"), "cross", CucumberCropBlock.AGE, new ArrayList<>());

        this.wildCropBlock(ModBlocks.WILD_RADISH.get());
        this.wildCropBlock(ModBlocks.WILD_CUCUMBERS.get());

        this.customModelBlock(ModBlocks.CLAY_POT.get(), "clay_pot");
        this.customModelBlock(ModBlocks.POT_ROAST.get(), "pot_roast");
        this.customModelBlock(ModBlocks.POT_ROAST_WITH_CARROT.get(), "pot_roast_with_carrot");
        this.customModelBlock(ModBlocks.POT_POTATOES_WITH_MUSHROOMS.get(), "pot_potatoes_with_mushrooms");

        this.customModelBlock(ModBlocks.JAR.get(), "jar");
        this.customModelBlock(ModBlocks.PICKLES_JAR.get(), "pickles_jar");
        this.customModelBlock(ModBlocks.BRINE_JAR.get(), "brine_jar");
        this.customModelBlock(ModBlocks.SAUERKRAUT_JAR.get(), "sauerkraut_jar");

        this.customHorizontalBlock(ModBlocks.BREW_BARREL.get(),
                $ -> existingModel(ModBlocks.BREW_BARREL.get()), BrewBarrelBlock.WATERLOGGED);
    }

    public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
        String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
        return ConfiguredModel.allYRotations(models().cubeAll(formattedName, resourceBlock(formattedName)), 0, false);
    }

    public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                            .build();
                }, ignored);
    }

    public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
                        .build(), ignored);
    }

    public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    int ageSuffix = state.getValue(ageProperty);
                    String stageName = blockName(block) + "_stage" + ageSuffix;
                    return ConfiguredModel.builder()
                            .modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
                }, ignored);
    }

    public void customStageBlock(Block block, @Nullable ResourceLocation parent, String textureKey, IntegerProperty ageProperty, List<Integer> suffixes, Property<?>... ignored) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    int ageSuffix = state.getValue(ageProperty);
                    String stageName = blockName(block) + "_stage";
                    stageName += suffixes.isEmpty() ? ageSuffix : suffixes.get(Math.min(suffixes.size(), ageSuffix));
                    if (parent == null) {
                        return ConfiguredModel.builder()
                                .modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
                    }
                    return ConfiguredModel.builder()
                            .modelFile(models().singleTexture(stageName, parent, textureKey, resourceBlock(stageName)).renderType("cutout")).build();
                }, ignored);
    }

    public void wildCropBlock(Block block) {
        this.wildCropBlock(block, false);
    }

    public void wildCropBlock(Block block, boolean isBushCrop) {
        if (isBushCrop) {
            this.simpleBlock(block, models().singleTexture(blockName(block), resourceBlock("bush_crop"), "crop", resourceBlock(blockName(block))).renderType("cutout"));
        } else {
            this.simpleBlock(block, models().cross(blockName(block), resourceBlock(blockName(block))).renderType("cutout"));
        }
    }

    public void crateBlock(Block block, String cropName) {
        this.simpleBlock(block,
                models().cubeBottomTop(blockName(block), resourceBlock(cropName + "_crate_side"), resourceBlock("crate_bottom"), resourceBlock(cropName + "_crate_top")));
    }

    public void customModelBlock(Block block, String modelName) {
        this.simpleBlock(block, models().getExistingFile(modLoc(modelName)));
    }

}
//package com.legomanchik.slavic_delight.common.world;
//
//import com.legomanchik.slavic_delight.common.registry.ModBlocks;
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.levelgen.structure.templatesystem.*;
//import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
//import vectorwing.farmersdelight.common.Configuration;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class VillageStructures {
//    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
//
//        if (Configuration.GENERATE_VILLAGE_FARM_FD_CROPS.get()) {
//            Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
//
//            StructureProcessor temperateCropProcessor = new RuleProcessor(List.of(
//                    new ProcessorRule(new RandomBlockMatchTest(vectorwing.farmersdelight.common.registry.ModBlocks.ONION_CROP.get(), 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.RADISH_CROP.get().defaultBlockState()),
//                    new ProcessorRule(new RandomBlockMatchTest(Blocks.CARROTS, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.CUCUMBER_CROP.get().defaultBlockState())
//            ));
//
//            StructureProcessor coldCropProcessor = new RuleProcessor(List.of(
//                    new ProcessorRule(new RandomBlockMatchTest(Blocks.POTATOES, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.RADISH_CROP.get().defaultBlockState())
//            ));
//
//            StructureProcessor aridCropProcessor = new RuleProcessor(List.of(
//                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.CUCUMBER_CROP.get().defaultBlockState()),
//                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.RADISH_CROP.get().defaultBlockState())
//            ));
//
//            addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_plains"), temperateCropProcessor, processorLists);
//            addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_savanna"), aridCropProcessor, processorLists);
//            addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_snowy"), coldCropProcessor, processorLists);
//            addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_taiga"), temperateCropProcessor, processorLists);
//            addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_desert"), aridCropProcessor, processorLists);
//        }
//    }
//
//    private static void addNewRuleToProcessorList(ResourceLocation targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
//        processorListRegistry.getOptional(targetProcessorList)
//                .ifPresent(processorList -> {
//                    try {
//                        Field listField = StructureProcessorList.class.getDeclaredField("list");
//                        listField.setAccessible(true);
//
//                        @SuppressWarnings("unchecked")
//                        List<StructureProcessor> currentList = (List<StructureProcessor>) listField.get(processorList);
//
//                        List<StructureProcessor> newList = new ArrayList<>(currentList);
//                        newList.add(processorToAdd);
//
//                        listField.set(processorList, newList);
//
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                    }
//                });
//    }
//}
//
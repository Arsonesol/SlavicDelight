package com.legomanchik.slavic_delight.data;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMaps extends DataMapProvider
{
    protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                // 30% chance
                .add(ModItems.BOILED_EGG.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
                // 50% chance
                .add(ModBlocks.WILD_CUCUMBERS.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
                .add(ModBlocks.WILD_RADISH.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
                // 65% chance
                .add(ModItems.CUCUMBER.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(ModItems.RADISH.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
                // 85% chance
                .add(ModItems.PICKLES.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
                // 100% chance
    }
}

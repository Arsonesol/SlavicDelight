package com.legomanchik.slavic_delight.data.loot;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.HashSet;
import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider
{
    private final Set<Block> generatedLootTables = new HashSet<>();

    public ModBlockLootProvider(HolderLookup.Provider holder) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), holder);
    }

    @Override
    protected void generate() {
        add(ModBlocks.CLAY_POT.get(), (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                        .include(DataComponents.CUSTOM_NAME)
                        .include(ModDataComponents.CONTAINER.get())
                )))));
        add(ModBlocks.BREW_BARREL.get(), (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                        .include(DataComponents.CUSTOM_NAME)
                        .include(ModDataComponents.CONTAINER.get())
                )))));

        dropSelf(ModBlocks.RADISH_CRATE.get());
        dropSelf(ModBlocks.CUCUMBER_CRATE.get());

        dropSelf(ModBlocks.PICKLES_JAR.get());
        dropSelf(ModBlocks.BRINE_JAR.get());
        dropSelf(ModBlocks.SAUERKRAUT_JAR.get());
    }

    protected void dropNamedContainer(Block block) {
        add(block, this::createNameableBlockEntityTable);
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }
}

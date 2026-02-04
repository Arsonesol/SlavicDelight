package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

public class PotWithEatBlockEntity extends SyncedBlockEntity {

    private int usageCount;

    public PotWithEatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POT_WITH_EAT_ENTITY.get(), pos, state);
        this.usageCount = 3;
    }

    public PotWithEatBlockEntity(BlockPos pos, BlockState state, int usageCount) {
        super(ModBlockEntities.POT_WITH_EAT_ENTITY.get(), pos, state);
        this.usageCount = usageCount;
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        compound.putInt("slavic_delight.usagecount", usageCount);
        super.saveAdditional(compound, registries);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        usageCount = compound.getInt("slavic_delight.usagecount");
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public void use() {
        if (usageCount > 0) {
            usageCount--;
        }

        if (this.usageCount == 0) {
            BlockPos pos = getBlockPos();
            BlockState state = ModBlocks.CLAY_POT.get().defaultBlockState();

            level.setBlockAndUpdate(pos, state);
            setChanged();
        }
    }
}

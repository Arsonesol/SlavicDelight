package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PotWithEatEntity extends CookingPotEntity {

    public PotWithEatEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.POT_WITH_EAT_ENTITY.get(), pPos, pBlockState);
    }
}

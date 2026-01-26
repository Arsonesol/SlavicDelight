package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class BrineJarEntity extends BlockEntity implements TickableBlockEntity {

    public BrineJarEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRINE_JAR_ENTITY.get(), pos, state);
    }

    @Override
    public void tick() {

    }
}

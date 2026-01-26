package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class JarBlockEntity extends BlockEntity {

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JAR_ENTITY.get(), pos, state);
    }
}

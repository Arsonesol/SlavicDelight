package com.legomanchik.slavic_delight.common.block.custom;

import com.legomanchik.slavic_delight.common.block.entity.SauerkrautJarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SauerkrautJarBlock extends JarBlock {
    public SauerkrautJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SauerkrautJarEntity(pos, state);
    }
}

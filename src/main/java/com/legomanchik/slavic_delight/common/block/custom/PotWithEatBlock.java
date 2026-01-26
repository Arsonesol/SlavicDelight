package com.legomanchik.slavic_delight.common.block.custom;

import com.legomanchik.slavic_delight.common.block.entity.PotWithEatEntity;
import com.legomanchik.slavic_delight.common.block.entity.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotWithEatBlock extends CookingPotBlock {

    private int eatNutrition;
    private float eatSaturation;

    public PotWithEatBlock(Properties properties) {
        super(properties);
    }

    public PotWithEatBlock(Properties properties, int eatNutrition, float eatSaturation) {
        super(properties);
        this.eatNutrition = eatNutrition;
        this.eatSaturation = eatSaturation;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PotWithEatEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return TickableBlockEntity.getTickerHelper(level);
    }
}

package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BrewBarrelEntity extends BlockEntity implements MenuProvider, TickableBlockEntity {
    public BrewBarrelEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BREW_BARREL_ENTITY.get(), pPos, pBlockState);
    }


    @Override
    public void tick() {

    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}

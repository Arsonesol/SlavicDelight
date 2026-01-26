package com.legomanchik.slavic_delight.common.block.entity;

import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

public class CookingPotEntity extends BlockEntity implements MenuProvider, HeatableBlockEntity {

    public CookingPotEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COOKING_POT_ENTITY.get(), pPos, pBlockState);
    }

    public CookingPotEntity(BlockEntityType<?> entityType, BlockPos pPos, BlockState pBlockState) {
        super(entityType, pPos, pBlockState);
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

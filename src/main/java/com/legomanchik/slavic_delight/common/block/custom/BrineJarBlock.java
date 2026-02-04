package com.legomanchik.slavic_delight.common.block.custom;

import com.legomanchik.slavic_delight.common.block.entity.BrineJarEntity;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BrineJarBlock extends JarBlock {

    public BrineJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BrineJarEntity(pos, state);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult result) {
        if (!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND) {
            BlockEntity be = pLevel.getBlockEntity(pPos);

            if (be instanceof BrineJarEntity blockEntity) {
                if (stack.is(Items.GLASS_BOTTLE)) {
                    pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);

                    blockEntity.minusBottle();
                    ItemStack item = new ItemStack(ModItems.BRINE_BOTTLE.get());

                    if (pPlayer.getInventory().getFreeSlot() == -1) {
                        var entity = new ItemEntity(pLevel, pPos.getX() + 0.5D, pPos.getY() + 1D, pPos.getZ() + 0.5D, item);
                        pLevel.addFreshEntity(entity);

                    } else {
                        pPlayer.addItem(item);
                    }

                    if (!pPlayer.isCreative()) {

                        ItemStack toInsert = stack.copy();
                        toInsert.setCount(1);

                        ItemStack remainder = stack.copy();
                        remainder.setCount(remainder.getCount() - 1);

                        pPlayer.setItemInHand(pHand, remainder);
                    }
                    pLevel.sendBlockUpdated(pPos, pState, pState, 3);

                    return ItemInteractionResult.SUCCESS;

                } else if (stack.is(ModItems.BRINE_BOTTLE.get())) {
                    if (blockEntity.getBottles() == 3) return ItemInteractionResult.FAIL;

                    pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);

                    blockEntity.plusBottle();
                    ItemStack item = new ItemStack(Items.GLASS_BOTTLE);

                    if (pPlayer.getInventory().getFreeSlot() == -1) {
                        var entity = new ItemEntity(pLevel, pPos.getX() + 0.5D, pPos.getY() + 1D, pPos.getZ() + 0.5D, item);
                        pLevel.addFreshEntity(entity);

                    } else {
                        pPlayer.addItem(item);
                    }

                    if (!pPlayer.isCreative()) {

                        ItemStack toInsert = stack.copy();
                        toInsert.setCount(1);

                        ItemStack remainder = stack.copy();
                        remainder.setCount(remainder.getCount() - 1);

                        pPlayer.setItemInHand(pHand, remainder);
                    }
                    pLevel.sendBlockUpdated(pPos, pState, pState, 3);

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            return;
        }
    }
}
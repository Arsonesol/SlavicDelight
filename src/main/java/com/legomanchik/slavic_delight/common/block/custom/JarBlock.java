package com.legomanchik.slavic_delight.common.block.custom;

import com.legomanchik.slavic_delight.common.block.entity.BrineJarEntity;
import com.legomanchik.slavic_delight.common.block.entity.JarBlockEntity;
import com.legomanchik.slavic_delight.common.datacomponents.JarDataComponent;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModDataComponentTypes;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 15, 12);

    public JarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity be = level.getBlockEntity(pos);

        if (player.isShiftKeyDown()) return ItemInteractionResult.FAIL;

        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            if(be instanceof JarBlockEntity blockEntity) {

                if (blockEntity.inventoryIsEmpty() && stack.isEmpty()) return ItemInteractionResult.FAIL;
                if (blockEntity.inventoryIsFull() && !stack.isEmpty() && !stack.is(Items.WATER_BUCKET) && !stack.is(Items.BUCKET)) return ItemInteractionResult.FAIL;

                ItemStack remainder = stack;
                if(stack.is(Items.WATER_BUCKET) && !blockEntity.hasWater()) {
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                    if (!player.isCreative()) {
                        if (stack.getCount() > 1) {
                            if (player.getInventory().getFreeSlot() != -1) {
                                player.addItem(new ItemStack(Items.BUCKET));
                                remainder.setCount(remainder.getCount() - 1);
                                player.setItemInHand(hand, remainder);
                            } else {
                                player.drop(new ItemStack(Items.BUCKET), false);
                            }
                        } else {
                            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                        }
                    }
                    blockEntity.setHasWater(!blockEntity.hasWater());
                    level.sendBlockUpdated(pos, state, state, 3);
                    return ItemInteractionResult.CONSUME;

                } else if (stack.is(Items.BUCKET) && blockEntity.hasWater()) {
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                    if (!player.isCreative()) {
                        if (stack.getCount() > 1) {
                            if (player.getInventory().getFreeSlot() != -1) {
                                player.addItem(new ItemStack(Items.WATER_BUCKET));
                                remainder.setCount(remainder.getCount() - 1);
                                player.setItemInHand(hand, remainder);
                            } else {
                                player.drop(new ItemStack(Items.WATER_BUCKET), false);
                            }
                        } else {
                            player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                        }
                    }
                    blockEntity.setHasWater(!blockEntity.hasWater());
                    level.sendBlockUpdated(pos, state, state, 3);
                    return ItemInteractionResult.CONSUME;

                } else if (stack.is(ModItems.BRINE_BOTTLE.get()) && !blockEntity.hasWater() && blockEntity.inventoryIsEmpty()) {
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);
                    level.setBlock(pos, ModBlocks.BRINE_JAR.get().defaultBlockState(), 3);
                    if (level.getBlockEntity(pos) instanceof BrineJarEntity brineJarEntity) {
                        brineJarEntity.setBottleCount(1);
                    }
                    return ItemInteractionResult.CONSUME;
                } else {
                    ItemStackHandler inventory = blockEntity.getInventory();

                    if(stack.isEmpty()) {
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
                        for(int i = 0; i < 3; i++) {
                            ItemStack extracted = inventory.extractItem(i, player.isCrouching() ? inventory.getSlotLimit(i) : 1, false);
                            player.addItem(extracted);

                        }
                        return ItemInteractionResult.CONSUME;

                    } else {
                        if (!blockEntity.inventoryIsFull()) {
                            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
                        }
                        ItemStack toInsert = stack.copy();
                        toInsert.setCount(1);

                        for(int i = 0; i < 3; i++) {
                            if(inventory.getStackInSlot(i).isEmpty()) {
                                ItemStack leftover = inventory.insertItem(i, toInsert, false);

                                remainder = stack.copy();
                                remainder.setCount(remainder.getCount() - 1);
                                remainder.grow(leftover.getCount());
                                player.setItemInHand(hand, remainder);
                                break;

                            }
                        }
                        return ItemInteractionResult.CONSUME;
                    }
                }
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = level.getFluidState(pos);

        if (!context.getLevel().getBlockState(pos.below()).isSolid() && !context.getLevel().getBlockState(pos.above()).is(Blocks.CHAIN)) return null;

        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(pState, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        if (pLevel.getBlockState(pPos.below()).isAir() && !pLevel.getBlockState(pPos.above()).is(Blocks.CHAIN)) {
            pLevel.destroyBlock(pPos, false);
        }
        super.neighborChanged(pState, pLevel, pPos, neighborBlock, neighborPos, isMoving);
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            ItemStack stack = new ItemStack(this);
            if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
                JarDataComponent jarData = new JarDataComponent(jarBlockEntity.getPicklingTime(), jarBlockEntity.hasWater(), jarBlockEntity.getDroppableInventory());
                stack.set(ModDataComponentTypes.JAR_DATA.get(), jarData);
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (entity instanceof Player player) {
            if (!player.level().isClientSide()) {
                if (blockEntity instanceof JarBlockEntity jarBlockEntity) {
                    JarDataComponent jarData = stack.get(ModDataComponentTypes.JAR_DATA.get());
                    if (jarData != null) {
                        jarBlockEntity.setHasWater(jarData.hasWater());
                        jarBlockEntity.setPicklingTime(jarData.picklingTime());

                        ItemStackHandler inventory = jarBlockEntity.getInventory();
                        int slot = 0;
                        for(ItemStack item : jarData.items()) {
                            if(inventory.getStackInSlot(slot).isEmpty()) {
                                inventory.insertItem(slot, item, false);
                            }
                            slot++;
                        }
                    }
                }
            }
        }
        super.setPlacedBy(level, pos, state, entity, stack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return createTickerHelper(blockEntity, ModBlockEntities.JAR_ENTITY.get(), JarBlockEntity::picklingTick);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
    }
}

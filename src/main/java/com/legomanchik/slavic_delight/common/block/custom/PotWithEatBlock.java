package com.legomanchik.slavic_delight.common.block.custom;

import com.legomanchik.slavic_delight.common.block.entity.PotWithEatBlockEntity;
import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.FoodDataComponent;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class PotWithEatBlock extends ClayPotBlock {

    private FoodDataComponent foodData;

    public PotWithEatBlock(Properties properties) {
        super(properties);
    }

    public PotWithEatBlock(Properties properties, FoodDataComponent foodData) {
        super(properties);
        this.foodData = foodData;
    }

    public FoodDataComponent getFoodData() {
        return foodData;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlockEntities.POT_WITH_EAT_ENTITY.get().create(pos, state);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (player.isShiftKeyDown()) return ItemInteractionResult.FAIL;
        else {
            if (!player.isCreative() && player.getFoodData().getFoodLevel() < 20) {
                level.playSound(player, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS);
                level.playSound(player, pos, SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS);

                if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                    if (level.getBlockEntity(pos) instanceof PotWithEatBlockEntity blockEntity) {
                        blockEntity.use();
                        player.getFoodData().eat(foodData.nutrition(), foodData.saturation());

                        if (!foodData.effects().isEmpty()) {
                            foodData.giveEffects(player);
                        }
                    }
                }
            } else {
                return ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof PotWithEatBlockEntity potEntity) {
                int usageCount = potEntity.getUsageCount();

                if (usageCount > 0) {
                    ClayPotDataComponent potData = new ClayPotDataComponent(this.asItem(), usageCount);
                    ItemStack stack = new ItemStack(this);
                    stack.set(ModDataComponentTypes.CLAY_POT_USAGE_COUNT.get(), potData);

                    if (stack.isDamageableItem()) {
                        stack.setDamageValue(potData.getItemDamage());
                    }
                    Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (entity instanceof Player player) {
            if (!player.level().isClientSide()) {
                if (blockEntity instanceof PotWithEatBlockEntity potEntity) {
                    ClayPotDataComponent potData = stack.get(ModDataComponentTypes.CLAY_POT_USAGE_COUNT.get());
                    if (potData != null) {
                        potEntity.setUsageCount(potData.getUsageCount());
                    }
                }
            }
        }
        super.setPlacedBy(level, pos, state, entity, stack);
    }
}

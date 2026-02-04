package com.legomanchik.slavic_delight.common.block.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class ClayPotItemHandler implements IItemHandler
{
    private static final int SLOTS_INPUT = 4;
    private final IItemHandler itemHandler;
    private final Direction side;

    public ClayPotItemHandler(IItemHandler itemHandler, Direction side) {
        this.itemHandler = itemHandler;
        this.side = side;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            if (slot < SLOTS_INPUT) {
                ItemStack currentStack = itemHandler.getStackInSlot(slot);
                if (!currentStack.isEmpty() && currentStack.getCount() >= 1) {
                    return stack;
                }
                if (stack.getCount() > 1) {
                    ItemStack singleStack = stack.copy();
                    singleStack.setCount(1);

                    ItemStack remaining = stack.copy();
                    remaining.setCount(stack.getCount() - 1);

                    ItemStack result = itemHandler.insertItem(slot, singleStack, simulate);

                    if (simulate) {
                        return remaining;
                    } else if (result.isEmpty()) {
                        return remaining;
                    } else {
                        return stack;
                    }
                }

                return itemHandler.insertItem(slot, stack, simulate);
            }
            return stack;
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            return slot < SLOTS_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}

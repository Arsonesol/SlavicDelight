package com.legomanchik.slavic_delight.common.block.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class BrewBarrelItemHandler implements IItemHandler {
    private static final int SLOTS_INPUT = 4;
    private static final int SLOT_CONTAINER_INPUT = 0;
    private static final int SLOT_BOTTLE_INPUT = 4;
    private static final int SLOT_OUTPUT = 5;
    private final IItemHandler itemHandler;
    private final Direction side;

    public BrewBarrelItemHandler(IItemHandler itemHandler, Direction side) {
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
            return slot < SLOTS_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        } else if (side.equals(Direction.SOUTH)) {
            return slot == SLOT_BOTTLE_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        } else {
            return slot == SLOT_CONTAINER_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null || side.equals(Direction.UP)) {
            return slot < SLOTS_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else if (side.equals(Direction.SOUTH)) {
                return slot == SLOT_BOTTLE_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot == SLOT_OUTPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }
}

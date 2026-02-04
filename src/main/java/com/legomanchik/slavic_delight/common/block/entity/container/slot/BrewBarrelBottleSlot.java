package com.legomanchik.slavic_delight.common.block.entity.container.slot;

import com.legomanchik.slavic_delight.common.tag.CommonTags;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BrewBarrelBottleSlot extends SlotItemHandler {
    public BrewBarrelBottleSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(CommonTags.BOTTLE);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}

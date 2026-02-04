package com.legomanchik.slavic_delight.common.block.entity.container.menu;

import com.legomanchik.slavic_delight.common.block.entity.BrewBarrelEntity;
import com.legomanchik.slavic_delight.common.block.entity.container.slot.BrewBarrelBottleSlot;
import com.legomanchik.slavic_delight.common.block.entity.container.slot.BrewBarrelResultSlot;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Objects;

public class BrewBarrelMenu extends RecipeBookMenu<RecipeWrapper, BrewBarrelRecipe> {
    public final BrewBarrelEntity blockEntity;
    public final ItemStackHandler inventory;
    private final ContainerData brewBarrelData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;

    public BrewBarrelMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
    }

    public BrewBarrelMenu(final int windowId, final Inventory playerInventory, final BrewBarrelEntity blockEntity, ContainerData brewBarrelDataIn) {
        super(ModMenuTypes.BREW_BARREL_MENU.get(), windowId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.brewBarrelData = brewBarrelDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        int startX = 8;
        int startY = 18;
        int inputStartX = 26;
        int inputStartY = 23;
        int borderSlotSize = 18;

        int slotIndex = 0;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 2; ++column) {
                this.addSlot(new SlotItemHandler(inventory, slotIndex++,
                        inputStartX + (column * borderSlotSize),
                        inputStartY + (row * borderSlotSize)));
            }
        }

        this.addSlot(new BrewBarrelBottleSlot(inventory, slotIndex++,
                inputStartX + 47, inputStartY + 30));

        this.addSlot(new BrewBarrelResultSlot(inventory, slotIndex++,
                inputStartX + 76, inputStartY + 9));

        int startPlayerInvY = startY * 4 + 12;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
                        startPlayerInvY + (row * borderSlotSize)));
            }
        }

        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
        }

        this.addDataSlots(brewBarrelDataIn);
    }

    private static BrewBarrelEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof BrewBarrelEntity) {
            return (BrewBarrelEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, ModBlocks.BREW_BARREL.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        final int INGREDIENT_SLOTS = 4;
        final int PLAYER_INV_END = INGREDIENT_SLOTS + 36;

        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            result = sourceStack.copy();

            if (index < INGREDIENT_SLOTS) {
                if (!this.moveItemStackTo(sourceStack, INGREDIENT_SLOTS, PLAYER_INV_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < PLAYER_INV_END) {
                if (!this.moveItemStackTo(sourceStack, 0, INGREDIENT_SLOTS, false)) {
                    if (index < INGREDIENT_SLOTS + 27) {
                        if (!this.moveItemStackTo(sourceStack,
                                INGREDIENT_SLOTS + 27, PLAYER_INV_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!this.moveItemStackTo(sourceStack,
                                INGREDIENT_SLOTS, INGREDIENT_SLOTS + 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
            if (sourceStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (sourceStack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, sourceStack);
        }
        return result;
    }

    public boolean isCrafting() {
        return brewBarrelData.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.brewBarrelData.get(0);
        int maxProgress = this.brewBarrelData.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            helper.accountSimpleStack(inventory.getStackInSlot(i));
        }
    }

    @Override
    public void clearCraftingContent() {
        for (int i = 0; i < 4; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(RecipeHolder<BrewBarrelRecipe> recipe) {
        return recipe.value().matches(new RecipeWrapper(inventory), level);
    }

    @Override
    public int getResultSlotIndex() {
        return 7;
    }

    @Override
    public int getGridWidth() {
        return 3;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return 7;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("SLAVIC_DELIGHT_BREWING");
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot < (getGridWidth() * getGridHeight());
    }
}

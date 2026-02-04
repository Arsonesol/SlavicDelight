package com.legomanchik.slavic_delight.common.block.entity;

import com.google.common.collect.Lists;
import com.legomanchik.slavic_delight.common.crafting.JarRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.List;
import java.util.Optional;

public class JarBlockEntity extends SyncedBlockEntity implements RecipeCraftingHolder {
    public static final int INVENTORY_SIZE = 3;

    private ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private int picklingTime;
    private int picklingTimeTotal;
    private boolean hasWater = false;

    protected final ContainerData jarData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private final RecipeManager.CachedCheck<RecipeWrapper, JarRecipe> quickCheck;

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JAR_ENTITY.get(), pos, state);
        this.inventory = createHandler();
        this.jarData = createIntArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(ModRecipes.PICKLING.get());
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
        setChanged();
    }

    public boolean hasWater() {
        return this.hasWater;
    }

    public int getPicklingTime() {
        return this.picklingTime;
    }

    public void setPicklingTime(int picklingTime) {
        this.picklingTime = picklingTime;
    }

    public ItemStack getRenderStack(int slot) {
        if (slot >= 0 && slot < inventory.getSlots()) {
            return inventory.getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        CompoundTag jarData = new CompoundTag();
        jarData.putInt("PickingTime", this.picklingTime);
        jarData.putBoolean("HasWater", this.hasWater);
        jarData.put("Inventory", this.inventory.serializeNBT(registries));

        tag.put("JarData", jarData);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("JarData", CompoundTag.TAG_COMPOUND)) {
            CompoundTag jarData = tag.getCompound("JarData");
            this.picklingTime = jarData.getInt("PicklingTime");
            this.hasWater = jarData.getBoolean("HasWater");

            if (jarData.contains("Inventory", CompoundTag.TAG_COMPOUND)) {
                this.inventory.deserializeNBT(registries, jarData.getCompound("Inventory"));
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public static void picklingTick(Level level, BlockPos pos, BlockState state, JarBlockEntity jar) {
        boolean didInventoryChange = false;

        if (jar.hasWater()) {
            Optional<RecipeHolder<JarRecipe>> recipe = jar.getMatchingRecipe(new RecipeWrapper(jar.inventory));
            boolean oneItemInSlot = true;
            for (int i = 0; i < jar.inventory.getSlots(); i++) {
                oneItemInSlot = jar.inventory.getStackInSlot(i).getCount() == 1;
            }
            if (oneItemInSlot) {
                if (recipe.isPresent() && jar.canPickling(recipe.get().value())) {
                    didInventoryChange = jar.processPickling(recipe.get(), jar);
                } else {
                    jar.picklingTime = Mth.clamp(jar.picklingTime - 2, 0, jar.picklingTimeTotal);
                }
            }
        } else if (jar.picklingTime > 0) {
            jar.picklingTime = Mth.clamp(jar.picklingTime - 2, 0, jar.picklingTimeTotal);
        }

        if (didInventoryChange) {
            jar.inventoryChanged();
        }
    }

    private Optional<RecipeHolder<JarRecipe>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();
        return quickCheck.getRecipeFor(inventoryWrapper, this.level);
    }

    protected boolean canPickling(JarRecipe recipe) {
        ItemStack resultStack = recipe.assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());
        return !resultStack.isEmpty();
    }

    private boolean processPickling(RecipeHolder<JarRecipe> recipe, JarBlockEntity clayPot) {
        if (level == null) return false;

        ++picklingTime;
        picklingTimeTotal = recipe.value().getCookTime();
        if (picklingTime < picklingTimeTotal) {
            return false;
        }

        picklingTime = 0;
        ItemStack resultStack = recipe.value().assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());

        setResult(resultStack);
        return true;
    }

    private void setResult(ItemStack result) {
        if (level != null && !level.isClientSide()) {
            if (result.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockPos pos = getBlockPos();

                BlockState currentState = level.getBlockState(pos);
                BlockState newState = block.defaultBlockState();

                if (currentState.hasProperty(BlockStateProperties.WATERLOGGED) &&
                        newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    newState = newState.setValue(BlockStateProperties.WATERLOGGED,
                            currentState.getValue(BlockStateProperties.WATERLOGGED));
                }

                level.removeBlockEntity(pos);

                level.setBlock(pos, newState, Block.UPDATE_ALL);

                clearInventory();
            }
        }
    }

    @Override
    public void setRecipeUsed(RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.id();
            usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void clearInventory() {
        for (int i = 0; i < 3; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<RecipeHolder<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
        player.awardRecipes(usedRecipes);
        usedRecipeTracker.clear();
    }

    public List<RecipeHolder<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipeTracker.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(list::add);
        }

        return list;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < INVENTORY_SIZE; ++i) {
            drops.add(inventory.getStackInSlot(i));
        }
        return drops;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public boolean inventoryIsEmpty() {
        return inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(1).isEmpty() && inventory.getStackInSlot(2).isEmpty();
    }

    public boolean inventoryIsFull() {
        return !inventory.getStackInSlot(0).isEmpty() && !inventory.getStackInSlot(1).isEmpty() && !inventory.getStackInSlot(2).isEmpty();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE)
        {
            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return super.getStackLimit(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> JarBlockEntity.this.picklingTime;
                    case 1 -> JarBlockEntity.this.picklingTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> JarBlockEntity.this.picklingTime = value;
                    case 1 -> JarBlockEntity.this.picklingTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
}

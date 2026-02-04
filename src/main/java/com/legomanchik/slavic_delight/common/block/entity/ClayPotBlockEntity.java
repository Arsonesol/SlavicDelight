package com.legomanchik.slavic_delight.common.block.entity;

import com.google.common.collect.Lists;
import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.ClayPotMenu;
import com.legomanchik.slavic_delight.common.block.inventory.ClayPotItemHandler;
import com.legomanchik.slavic_delight.common.crafting.ClayPotRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import javax.annotation.Nullable;
import java.util.List;

import java.util.Optional;


@EventBusSubscriber(modid = SlavicDelight.MOD_ID)
public class ClayPotBlockEntity extends SyncedBlockEntity implements MenuProvider, HeatableBlockEntity, Nameable, RecipeCraftingHolder {
    public static final int INVENTORY_SIZE = 4;

    private final ItemStackHandler inventory;
    private final IItemHandler inputHandler;
    private final IItemHandler outputHandler;

    private int cookTime;
    private int cookTimeTotal;
    private Component customName;

    protected final ContainerData cookingPotData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private final RecipeManager.CachedCheck<RecipeWrapper, ClayPotRecipe> quickCheck;

    public ClayPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLAY_POT.get(), pos, state);
        this.inventory = createHandler();
        this.inputHandler = new ClayPotItemHandler(inventory, Direction.UP);
        this.outputHandler = new ClayPotItemHandler(inventory, Direction.DOWN);
        this.cookingPotData = createIntArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(ModRecipes.CLAY_POT_COOKING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CLAY_POT.get(),
                (be, context) -> {
                    if (context == Direction.UP) {
                        return be.inputHandler;
                    }
                    return be.outputHandler;
                }
        );
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        cookTime = compound.getInt("CookTime");
        cookTimeTotal = compound.getInt("CookTimeTotal");
        if (compound.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"), registries);
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            usedRecipeTracker.put(ResourceLocation.parse(key), compoundRecipes.getInt(key));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("CookTime", cookTime);
        compound.putInt("CookTimeTotal", cookTimeTotal);
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName, registries));
        }
        compound.put("Inventory", inventory.serializeNBT(registries));
        CompoundTag compoundRecipes = new CompoundTag();
        usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    private CompoundTag writeItems(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("Inventory", inventory.serializeNBT(registries));
        return compound;
    }

    public ItemStack getAsItem() {
        ItemStack stack = new ItemStack(ModBlocks.CLAY_POT.get());
        stack.applyComponents(collectComponents());
        return stack;
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, ClayPotBlockEntity cookingPot) {
        boolean isHeated = cookingPot.isHeated(level, pos);
        boolean didInventoryChange = false;

        if (isHeated) {
            Optional<RecipeHolder<ClayPotRecipe>> recipe = cookingPot.getMatchingRecipe(new RecipeWrapper(cookingPot.inventory));
            boolean oneItemInSlot = true;
            for (int i = 0; i < cookingPot.inventory.getSlots(); i++) {
                oneItemInSlot = cookingPot.inventory.getStackInSlot(i).getCount() == 1;
            }
            if (oneItemInSlot) {
                if (recipe.isPresent() && cookingPot.canCook(recipe.get().value())) {
                    didInventoryChange = cookingPot.processCooking(recipe.get(), cookingPot);
                } else {
                    cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
                }
            }
        } else if (cookingPot.cookTime > 0) {
            cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
        }

        if (didInventoryChange) {
            cookingPot.inventoryChanged();
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, ClayPotBlockEntity cookingPot) {
        if (cookingPot.isHeated(level, pos)) {
            RandomSource random = level.random;
            if (random.nextFloat() < 0.2F) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                double y = (double) pos.getY() + 0.7D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.0D, 0.0D);
            }
            if (random.nextFloat() < 0.05F) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double y = (double) pos.getY() + 0.5D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double motionY = random.nextBoolean() ? 0.015D : 0.005D;
                level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
            }
        }

    }

    private Optional<RecipeHolder<ClayPotRecipe>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();
        return quickCheck.getRecipeFor(inventoryWrapper, this.level);
    }

    protected boolean canCook(ClayPotRecipe recipe) {
        ItemStack resultStack = recipe.assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());
        return !resultStack.isEmpty();
    }

    private boolean processCooking(RecipeHolder<ClayPotRecipe> recipe, ClayPotBlockEntity clayPot) {
        if (level == null) return false;

        ++cookTime;
        cookTimeTotal = recipe.value().getCookTime();
        if (cookTime < cookTimeTotal) {
            return false;
        }

        cookTime = 0;
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

    private void clearInventory() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            stack.setCount(this.inventory.getStackInSlot(i).getCount() - 1);

            this.inventory.setStackInSlot(i, stack);
        }
        this.setChanged();
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.id();
            usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
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

    public boolean isHeated() {
        if (level == null) return false;
        return this.isHeated(level, worldPosition);
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
    public Component getName() {
        return customName != null ? customName : Component.translatable("container.slavic_delight.clay_pot");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new ClayPotMenu(id, player, this, cookingPotData);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return writeItems(new CompoundTag(), registries);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
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
        return new ContainerData()
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ClayPotBlockEntity.this.cookTime;
                    case 1 -> ClayPotBlockEntity.this.cookTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ClayPotBlockEntity.this.cookTime = value;
                    case 1 -> ClayPotBlockEntity.this.cookTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
}
package com.legomanchik.slavic_delight.common.block.entity;

import com.google.common.collect.Lists;
import com.legomanchik.slavic_delight.common.block.custom.BrewBarrelBlock;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.BrewBarrelMenu;
import com.legomanchik.slavic_delight.common.block.inventory.BrewBarrelItemHandler;
import com.legomanchik.slavic_delight.common.crafting.BrewBarrelRecipe;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class BrewBarrelEntity extends SyncedBlockEntity implements MenuProvider, Nameable, RecipeCraftingHolder
{
    public static final int BOTTLE_SLOT = 4;
    public static final int OUTPUT_SLOT = 5;
    public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

    public static final Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES = Map.ofEntries(
            entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET),
            entry(Items.AXOLOTL_BUCKET, Items.BUCKET),
            entry(Items.COD_BUCKET, Items.BUCKET),
            entry(Items.PUFFERFISH_BUCKET, Items.BUCKET),
            entry(Items.SALMON_BUCKET, Items.BUCKET),
            entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET),
            entry(Items.SUSPICIOUS_STEW, Items.BOWL),
            entry(Items.MUSHROOM_STEW, Items.BOWL),
            entry(Items.RABBIT_STEW, Items.BOWL),
            entry(Items.BEETROOT_SOUP, Items.BOWL),
            entry(Items.POTION, Items.GLASS_BOTTLE),
            entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE),
            entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE),
            entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE)
    );

    private final ItemStackHandler inventory;
    private final IItemHandler inputHandler;
    private final IItemHandler outputHandler;

    private int brewTime;
    private int brewTimeTotal;
    private ItemStack bottleStack;
    private Component customName;

    protected final ContainerData brewBarrelData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private final RecipeManager.CachedCheck<RecipeWrapper, BrewBarrelRecipe> quickCheck;

    public BrewBarrelEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREW_BARREL_ENTITY.get(), pos, state);
        this.inventory = createHandler();
        this.inputHandler = new BrewBarrelItemHandler(inventory, Direction.UP);
        this.outputHandler = new BrewBarrelItemHandler(inventory, Direction.DOWN);
        this.bottleStack = ItemStack.EMPTY;
        this.brewBarrelData = createIntArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.quickCheck = RecipeManager.createCheck(ModRecipes.BREWING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BREW_BARREL_ENTITY.get(),
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
        brewTime = compound.getInt("BrewTime");
        brewTimeTotal = compound.getInt("BrewTimeTotal");
        bottleStack = ItemStack.parseOptional(registries, compound.getCompound("Bottle"));
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
        compound.putInt("BrewTime", brewTime);
        compound.putInt("BrewTotal", brewTimeTotal);
        compound.put("Bottle", bottleStack.saveOptional(registries));
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
        compound.put("Bottle", bottleStack.saveOptional(registries));
        compound.put("Inventory", inventory.serializeNBT(registries));
        return compound;
    }

    public static void brewingTick(Level level, BlockPos pos, BlockState state, BrewBarrelEntity brewBarrel) {
        boolean didInventoryChange = false;

        if (brewBarrel.inventory.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() != brewBarrel.inventory.getStackInSlot(OUTPUT_SLOT).getCount()) {
            if (brewBarrel.hasInput()) {
                Optional<RecipeHolder<BrewBarrelRecipe>> recipe = brewBarrel.getMatchingRecipe(new RecipeWrapper(brewBarrel.inventory));
                if (recipe.isPresent() && brewBarrel.canBrew(recipe.get().value())) {
                    didInventoryChange = brewBarrel.processBrewing(recipe.get(), brewBarrel);
                } else {
                    brewBarrel.brewTime = Mth.clamp(brewBarrel.brewTime - 2, 0, brewBarrel.brewTimeTotal);
                }
            } else if (brewBarrel.brewTime > 0) {
                brewBarrel.brewTime = Mth.clamp(brewBarrel.brewTime - 2, 0, brewBarrel.brewTimeTotal);
            }
        }

        if (didInventoryChange) {
            brewBarrel.inventoryChanged();
        }
    }

    private Optional<RecipeHolder<BrewBarrelRecipe>> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();
        return hasInput() ? quickCheck.getRecipeFor(inventoryWrapper, this.level) : Optional.empty();
    }

    public ItemStack getBottleStack() {
        return this.bottleStack;
    }

    private boolean hasInput() {
        for (int i = 0; i < BOTTLE_SLOT; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    protected boolean canBrew(BrewBarrelRecipe recipe) {
        if (hasInput()) {
            ItemStack resultStack = recipe.assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());
            return !resultStack.isEmpty();
        } else {
            return false;
        }
    }

    private boolean processBrewing(RecipeHolder<BrewBarrelRecipe> recipe, BrewBarrelEntity brewBarrel) {
        if (level == null) return false;

        ++brewTime;
        brewTimeTotal = recipe.value().getBrewingTime();
        if (brewTime < brewTimeTotal) {
            return false;
        }

        brewTime = 0;
        ItemStack resultStack = recipe.value().assemble(new RecipeWrapper(this.inventory), this.level.registryAccess());

        ItemStack bottleInSlot = inventory.getStackInSlot(BOTTLE_SLOT);
        ItemStack requiredBottle = recipe.value().getBottle();

        if (!requiredBottle.isEmpty() && !bottleInSlot.isEmpty()) {
            bottleInSlot.shrink(requiredBottle.getCount());
            if (bottleInSlot.isEmpty()) {
                inventory.setStackInSlot(BOTTLE_SLOT, ItemStack.EMPTY);
            }
        }

        bottleStack = recipe.value().getContainerOverride();

        addToOutputSlot(resultStack);

        brewBarrel.setRecipeUsed(recipe);

        for (int i = 0; i < BOTTLE_SLOT; ++i) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
            } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                ejectIngredientRemainder(INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem()).getDefaultInstance());
            }
            if (!slotStack.isEmpty())
                slotStack.shrink(1);
        }
        return true;
    }

    private void addToOutputSlot(ItemStack resultStack) {
        ItemStack currentOutput = inventory.getStackInSlot(OUTPUT_SLOT);

        if (currentOutput.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, resultStack.copy());
        } else if (ItemStack.isSameItemSameComponents(currentOutput, resultStack)) {
            int newCount = currentOutput.getCount() + resultStack.getCount();
            int maxStackSize = currentOutput.getMaxStackSize();

            if (newCount <= maxStackSize) {
                currentOutput.setCount(newCount);
                inventory.setStackInSlot(OUTPUT_SLOT, currentOutput);
            } else {
                int remaining = newCount - maxStackSize;
                currentOutput.setCount(maxStackSize);
                inventory.setStackInSlot(OUTPUT_SLOT, currentOutput);

                ItemStack remainder = resultStack.copy();
                remainder.setCount(remaining);
                ejectIngredientRemainder(remainder);
            }
        } else {
            ejectIngredientRemainder(resultStack.copy());
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(BrewBarrelBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        ItemUtils.spawnItemEntity(level, remainderStack, x, y, z,
                direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
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
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((BrewBarrelRecipe) recipe.value()).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        ExperienceOrb.award(level, pos, expTotal);
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
        return customName != null ? customName : Component.translatable("container.slavic_delight.brew_barrel");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new BrewBarrelMenu(id, player, this, brewBarrelData);
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
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
        this.bottleStack = componentInput.getOrDefault(ModDataComponents.CONTAINER, ItemStackWrapper.EMPTY).getStack();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.customName);
        if (!getBottleStack().isEmpty()) {
            components.set(ModDataComponents.CONTAINER, new ItemStackWrapper(getBottleStack()));
        }
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
                    case 0 -> BrewBarrelEntity.this.brewTime;
                    case 1 -> BrewBarrelEntity.this.brewTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BrewBarrelEntity.this.brewTime = value;
                    case 1 -> BrewBarrelEntity.this.brewTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
}

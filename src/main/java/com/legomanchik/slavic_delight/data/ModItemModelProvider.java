package com.legomanchik.slavic_delight.data;

import com.google.common.collect.Sets;
import com.legomanchik.slavic_delight.SlavicDelight;

import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModItemModelProvider extends ItemModelProvider {

    public static final String GENERATED = "item/generated";
    public static final String HANDHELD = "item/handheld";

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SlavicDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> items = BuiltInRegistries.ITEM.stream().filter(i -> SlavicDelight.MOD_ID.equals(BuiltInRegistries.ITEM.getKey(i).getNamespace()))
                .collect(Collectors.toSet());

        basicItem(ModItems.BOILED_EGG.get());
        basicItem(ModItems.BOILED_POTATOES.get());
        basicItem(ModItems.CUCUMBER.get());
        basicItem(ModItems.PICKLES.get());
        basicItem(ModItems.RADISH.get());
        basicItem(ModItems.CUCUMBER_SEEDS.get());
        basicItem(ModItems.BRINE_BOTTLE.get());
        basicItem(ModItems.PANCAKES.get());
        basicItem(ModItems.PANCAKES_WITH_BERRIES.get());
        basicItem(ModItems.OKROSHKA_KVASS.get());
        basicItem(ModItems.OKROSHKA_KEFIR.get());
        basicItem(ModItems.SVEKOLNIK.get());
        basicItem(ModItems.BORSCH.get());
        basicItem(ModItems.SOLYANKA.get());
        basicItem(ModItems.CRAB_SALAD.get());
        basicItem(ModItems.CRAB_STICKS.get());
        basicItem(ModItems.MINCED_PORK.get());
        basicItem(ModItems.MINCED_CHICKEN.get());
        basicItem(ModItems.MINCED_FISH.get());
        basicItem(ModItems.MASHED_POTATOES.get());
        basicItem(ModItems.MASH_AND_PATTY.get());
        basicItem(ModItems.PIROZHOK_WITH_ONIONS.get());
        basicItem(ModItems.PIROZHOK_WITH_POTATOES.get());
        basicItem(ModItems.KURNIK.get());
        basicItem(ModItems.PELMENI.get());
        basicItem(ModItems.KVASS_BOTTLE.get());
        basicItem(ModItems.KEFIR_BOTTLE.get());
        basicItem(ModItems.PORK_PATTY.get());
        basicItem(ModItems.CHICKEN_PATTY.get());
        basicItem(ModItems.SAUSAGE.get());
        basicItem(ModItems.SAUERKRAUT.get());

        blockBasedModel(Item.byBlock(ModBlocks.CUCUMBER_CRATE.get()), "");
        blockBasedModel(Item.byBlock(ModBlocks.RADISH_CRATE.get()), "");

        Set<Item> flatBlockItems = Sets.newHashSet(
                ModBlocks.WILD_CUCUMBERS.get().asItem(),
                ModBlocks.WILD_RADISH.get().asItem()
        );
        takeAll(items, flatBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(item, resourceBlock(itemName(item))));
    }

    public void blockBasedModel(Item item, String suffix) {
        withExistingParent(itemName(item), resourceBlock(itemName(item) + suffix));
    }

    public void itemHandheldModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), HANDHELD).texture("layer0", texture);
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }

    private String itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "block/" + path);
    }

    public ResourceLocation resourceItem(String path) {
        return ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, "item/" + path);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                FarmersDelight.LOGGER.warn("Item {} not found in set", item);
            }
        }
        if (!src.removeAll(ret)) {
            FarmersDelight.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }
        return ret;
    }
}

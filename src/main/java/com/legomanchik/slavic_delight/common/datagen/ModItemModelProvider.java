package com.legomanchik.slavic_delight.common.datagen;

import com.legomanchik.slavic_delight.SlavicDelight;

import com.legomanchik.slavic_delight.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SlavicDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.BOILED_EGG.get());
        basicItem(ModItems.BOILED_POTATOES.get());
        basicItem(ModItems.CUCUMBER.get());
        basicItem(ModItems.PICKLES.get());
        basicItem(ModItems.RADISH.get());
        basicItem(ModItems.CUCUMBER_SEEDS.get());
        basicItem(ModItems.RADISH_SEEDS.get());
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
        basicItem(ModItems.PUSHER.get());
        basicItem(ModItems.KVASS_BOTTLE.get());
        basicItem(ModItems.KEFIR_BOTTLE.get());
        basicItem(ModItems.PORK_PATTY.get());
        basicItem(ModItems.CHICKEN_PATTY.get());
        basicItem(ModItems.SAUSAGE.get());
    }
}

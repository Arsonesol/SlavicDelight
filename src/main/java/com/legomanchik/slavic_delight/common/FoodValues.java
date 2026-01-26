package com.legomanchik.slavic_delight.common;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FoodValues {

    public static final FoodProperties CUCUMBER = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.4f).build();
    public static final FoodProperties PICKLES = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.4f).build();
    public static final FoodProperties RADISH = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.3f).build();
    public static final FoodProperties MINCED_PORK = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3f).fast().build();
    public static final FoodProperties MINCED_CHICKEN = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.3f).fast().build();
    public static final FoodProperties MINCED_FISH = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1f).fast().build();
    public static final FoodProperties CRAB_STICKS = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.2f).fast().build();
    public static final FoodProperties CHICKEN_PATTY = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.8f).fast().build();
    public static final FoodProperties PORK_PATTY = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.8f).fast().build();
    public static final FoodProperties SAUSAGE = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.5f).build();
    public static final FoodProperties KURNIK = (new FoodProperties.Builder()).nutrition(11).saturationModifier(0.7f).build();
    public static final FoodProperties CRAB_SALAD = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.8f).build();

    public static final FoodProperties OKROSHKA_KVASS = (new FoodProperties.Builder()).nutrition(15).saturationModifier(1f).effect(() -> {
        return new MobEffectInstance(ModEffects.COMFORT, 3600, 0);
    }, 1.0F).build();
    public static final FoodProperties OKROSHKA_KEFIR = (new FoodProperties.Builder()).nutrition(15).saturationModifier(1f).effect(() -> {
        return new MobEffectInstance(ModEffects.COMFORT, 3600, 0);
    }, 1.0F).build();
    public static final FoodProperties BORSCH = (new FoodProperties.Builder()).nutrition(13).saturationModifier(0.9f).effect(() -> {
        return new MobEffectInstance(ModEffects.COMFORT, 3600, 0);
    }, 1.0F).build();
    public static final FoodProperties SVEKOLNIK = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.9f).effect(() -> {
        return new MobEffectInstance(ModEffects.COMFORT, 3600, 0);
    }, 1.0F).build();
    public static final FoodProperties SOLYANKA = (new FoodProperties.Builder()).nutrition(10).saturationModifier(1f).effect(() -> {
        return new MobEffectInstance(ModEffects.COMFORT, 3600, 0);
    }, 1.0F).build();

    public static final FoodProperties MASHED_POTATOES = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.5f).build();
    public static final FoodProperties MASH_AND_PATTY = (new FoodProperties.Builder()).nutrition(8).saturationModifier(1.2f).build();
    public static final FoodProperties PIROZHOK_WITH_ONIONS = (new FoodProperties.Builder()).nutrition(5).saturationModifier(1f).build();
    public static final FoodProperties PIROZHOK_WITH_POTATOES = (new FoodProperties.Builder()).nutrition(5).saturationModifier(1f).build();
    public static final FoodProperties PANCAKES = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.5f).fast().build();
    public static final FoodProperties PANCAKES_WITH_BERRIES = (new FoodProperties.Builder()).nutrition(5).saturationModifier(0.9f).fast().build();
    public static final FoodProperties PELMENI = (new FoodProperties.Builder()).nutrition(9).saturationModifier(0.9f).build();
    public static final FoodProperties BOILED_POTATOES = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.5f).build();
    public static final FoodProperties BOILED_EGG = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.2f).build();
    public static final FoodProperties SAUERKRAUT_JAR = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.2f).build();
}

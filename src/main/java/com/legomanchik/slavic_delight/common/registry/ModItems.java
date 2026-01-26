package com.legomanchik.slavic_delight.common.registry;


import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.FoodValues;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final Supplier<Item> BOILED_POTATOES = registerWithTab("boiled_potatoes",
            ()-> new Item(foodItem(FoodValues.BOILED_POTATOES)));
    public static final Supplier<Item> BOILED_EGG = registerWithTab("boiled_egg",
            ()-> new Item(foodItem(FoodValues.BOILED_EGG)));
    public static final Supplier<Item> CUCUMBER = registerWithTab("cucumber",
            ()-> new Item(foodItem(FoodValues.CUCUMBER)));
    public static final Supplier<Item> PICKLES = registerWithTab("pickles",
            ()-> new Item(foodItem(FoodValues.PICKLES)));
    public static final Supplier<Item> RADISH = registerWithTab("radish",
            ()-> new Item(foodItem(FoodValues.RADISH)));
    public static final Supplier<Item> MINCED_PORK = registerWithTab("minced_pork",
            ()-> new Item(foodItem(FoodValues.MINCED_PORK)));
    public static final Supplier<Item> PORK_PATTY = registerWithTab("pork_patty",
            ()-> new Item(foodItem(FoodValues.PORK_PATTY)));
    public static final Supplier<Item> SAUSAGE = registerWithTab("sausage",
            ()-> new Item(foodItem(FoodValues.SAUSAGE)));
    public static final Supplier<Item> MINCED_CHICKEN = registerWithTab("minced_chicken",
            ()-> new Item(foodItem(FoodValues.MINCED_CHICKEN)));
    public static final Supplier<Item> MINCED_FISH = registerWithTab("minced_cod",
            ()-> new Item(foodItem(FoodValues.MINCED_FISH)));
    public static final Supplier<Item> CRAB_STICKS = registerWithTab("crab_sticks",
            ()-> new Item(foodItem(FoodValues.CRAB_STICKS)));
    public static final Supplier<Item> CHICKEN_PATTY = registerWithTab("chicken_patty",
            ()-> new Item(foodItem(FoodValues.CHICKEN_PATTY)));
    public static final Supplier<Item> KURNIK = ITEMS.register("kurnik",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.KURNIK), true));
    public static final Supplier<Item> KVASS_BOTTLE = registerWithTab("kvass_bottle",
            ()-> new DrinkableItem(drinkItem()));
    public static final Supplier<Item> KEFIR_BOTTLE = registerWithTab("kefir_bottle",
            ()-> new DrinkableItem(drinkItem()));
    public static final Supplier<Item> BRINE_BOTTLE = registerWithTab("brine_bottle",
            ()-> new DrinkableItem(drinkItem()));
    public static final Supplier<Item> CRAB_SALAD = registerWithTab("crab_salad",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.CRAB_SALAD)));
    public static final Supplier<Item> OKROSHKA_KVASS = registerWithTab("okroshka_kvass",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.OKROSHKA_KVASS), true));
    public static final Supplier<Item> OKROSHKA_KEFIR = registerWithTab("okroshka_kefir",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.OKROSHKA_KEFIR), true));
    public static final Supplier<Item> BORSCH = registerWithTab("borsch",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.BORSCH), true));
    public static final Supplier<Item> SVEKOLNIK = registerWithTab("svekolnik",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.SVEKOLNIK), true));
    public static final Supplier<Item> SOLYANKA = registerWithTab("solyanka",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.SOLYANKA), true));
    public static final Supplier<Item> MASHED_POTATOES = registerWithTab("mashed_potatoes",
            ()-> new Item(foodItem(FoodValues.MASHED_POTATOES)));
    public static final Supplier<Item> MASH_AND_PATTY = registerWithTab("mash_and_patty",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.MASH_AND_PATTY)));
    public static final Supplier<Item> PIROZHOK_WITH_ONIONS = registerWithTab("pirozhok_with_onions",
            ()-> new Item(foodItem(FoodValues.PIROZHOK_WITH_ONIONS)));
    public static final Supplier<Item> PIROZHOK_WITH_POTATOES = registerWithTab("pirozhok_with_potatoes",
            ()-> new Item(foodItem(FoodValues.PIROZHOK_WITH_POTATOES)));
    public static final Supplier<Item> PANCAKES = registerWithTab("pancakes",
            ()-> new Item(foodItem(FoodValues.PANCAKES)));
    public static final Supplier<Item> PANCAKES_WITH_BERRIES = registerWithTab("pancakes_with_berries",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.PANCAKES_WITH_BERRIES).stacksTo(8)));
    public static final Supplier<Item> PELMENI = registerWithTab("pelmeni",
            ()-> new ConsumableItem(bowlFoodItem(FoodValues.PELMENI)));

    public static final Supplier<Item> RADISH_SEEDS = registerWithTab("radish_seeds",
            ()-> new BlockItem(ModBlocks.RADISH_CROP.get(), new Item.Properties()
                    .rarity(Rarity.COMMON)));

    public static final Supplier<Item> CUCUMBER_SEEDS = registerWithTab("cucumber_seeds",
            ()-> new BlockItem(ModBlocks.CUCUMBER_CROP.get(), new Item.Properties()
                    .rarity(Rarity.COMMON)));

    public static final Supplier<Item> PUSHER = registerWithTab("pusher",
            ()-> new Item(new Item.Properties().stacksTo(1)
                    .rarity(Rarity.COMMON).durability(250)));

    public static Item.Properties foodItem(FoodProperties food) {
        return new Item.Properties().food(food);
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    public static Item.Properties drinkItem() {
        return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Supplier<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        Supplier<Item> item = ITEMS.register(name, supplier);
        ModCreativeTabs.addToTab(item);
        return item;
    }
}

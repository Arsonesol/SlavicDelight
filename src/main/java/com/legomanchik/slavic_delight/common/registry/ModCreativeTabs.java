package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

    public static final List<Supplier<? extends ItemLike>> TAB_ITEMS = new ArrayList<>();

    public static final Supplier<CreativeModeTab> TAB_SLAVICDELIGHT = CREATIVE_TABS.register(SlavicDelight.MOD_ID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.slavic_delight"))
                    .icon(() -> new ItemStack(ModItems.CUCUMBER.get()))
                    .displayItems((displayParams, output) ->
                            TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get()))).build());

    public static <T extends Item> Supplier<T> addToTab(Supplier<T> itemLike) {
        TAB_ITEMS.add(itemLike);
        return itemLike;
    }
}
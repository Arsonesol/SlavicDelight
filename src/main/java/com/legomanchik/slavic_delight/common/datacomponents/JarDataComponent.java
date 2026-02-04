package com.legomanchik.slavic_delight.common.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public record JarDataComponent(int picklingTime, boolean hasWater, NonNullList<ItemStack> items) implements TooltipComponent {
    public static final Codec<JarDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("pickling_time").forGetter(JarDataComponent::picklingTime),
                    Codec.BOOL.fieldOf("has_water").forGetter(JarDataComponent::hasWater),
                    ItemStack.OPTIONAL_CODEC.listOf()
                            .fieldOf("items")
                            .xmap(
                                    list -> list.stream()
                                            .map(stack -> stack == null ? ItemStack.EMPTY : stack)
                                            .toList(),
                                    jarItems -> jarItems.stream()
                                            .filter(stack -> !stack.isEmpty())
                                            .collect(Collectors.toList())
                            )
                            .forGetter(JarDataComponent::items)
            ).apply(instance, JarDataComponent::new)
    );

    public JarDataComponent {
        if (items == null) {
            items = NonNullList.withSize(3, ItemStack.EMPTY);
        } else if (items.size() != 3) {
            NonNullList<ItemStack> nonNullItems = NonNullList.withSize(3, ItemStack.EMPTY);
            for (int i = 0; i < Math.min(items.size(), 3); i++) {
                nonNullItems.set(i, items.get(i));
            }
            items = nonNullItems;
        }
    }

    public JarDataComponent(int picklingTime, boolean hasWater) {
        this(picklingTime, hasWater, NonNullList.withSize(3, ItemStack.EMPTY));
    }

    public JarDataComponent(int picklingTime, boolean hasWater, List<ItemStack> items) {
        this(picklingTime, hasWater,
                items instanceof NonNullList<ItemStack> ? (NonNullList<ItemStack>) items :
                        NonNullList.of(ItemStack.EMPTY, items.toArray(new ItemStack[0])));
    }
}

package com.legomanchik.slavic_delight.common.datacomponents;

import com.legomanchik.slavic_delight.common.block.entity.PotWithEatBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record ClayPotDataComponent(Item pot, int usageCount) implements TooltipComponent {
    public static final Codec<ClayPotDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("pot").forGetter(ClayPotDataComponent::pot),
                    Codec.INT.fieldOf("count").forGetter(ClayPotDataComponent::usageCount)
            ).apply(instance, ClayPotDataComponent::new)
    );

    @Override
    public int hashCode() {
        return java.util.Objects.hash(usageCount);
    }

    public ClayPotDataComponent() {
        this(Items.AIR, 3);
    }

    public static ClayPotDataComponent fromBlockEntity(PotWithEatBlockEntity clayPotEntity) {
        return new ClayPotDataComponent(clayPotEntity.getBlockState().getBlock().asItem(), clayPotEntity.getUsageCount());
    }

    public Item getPot() {
        return this.pot;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public int getItemDamage() {
        return Math.abs(3 - usageCount);
    }
}

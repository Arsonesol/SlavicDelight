package com.legomanchik.slavic_delight.common.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record FoodDataComponent(int nutrition, float saturation, List<MobEffectInstance> effects) {
    public static final Codec<FoodDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("nutrition").forGetter(FoodDataComponent::nutrition),
                    Codec.FLOAT.fieldOf("saturation").forGetter(FoodDataComponent::saturation),
                    MobEffectInstance.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodDataComponent::effects)
            ).apply(instance, FoodDataComponent::new)
    );

    public void giveEffects(Player player) {
        for (MobEffectInstance effectInstance : effects) {
            player.addEffect(effectInstance);
        }
    }
}

package com.legomanchik.slavic_delight.common.item;

import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.FoodDataComponent;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.legomanchik.slavic_delight.common.registry.ModDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class BlockFoodItem extends BlockItem {
    private final FoodDataComponent foodData;

    public BlockFoodItem(Block block, Properties properties, FoodDataComponent foodData) {
        super(block, properties
                .component(ModDataComponentTypes.FOOD_DATA.get(), foodData)
                .food(createFoodProperties(foodData)));
        this.foodData = foodData;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    private static FoodProperties createFoodProperties(FoodDataComponent foodData) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(foodData.nutrition())
                .saturationModifier(foodData.saturation())
                .alwaysEdible();

        for (MobEffectInstance effect : foodData.effects()) {
            builder.effect(effect, 1.0f);
        }
        return builder.build();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            player.getFoodData().eat(foodData.nutrition(), foodData.saturation());
        }
        for (MobEffectInstance effect : foodData.effects()) {
            entity.addEffect(effect);
        }
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F,
                level.random.nextFloat() * 0.1F + 0.9F);

        int damage = stack.getOrDefault(DataComponents.DAMAGE, 0) + 1;
        if (damage >= stack.getMaxDamage()) {
            if (!level.isClientSide) {
                level.playSound(null, entity.blockPosition(),
                        SoundEvents.DECORATED_POT_BREAK, SoundSource.BLOCKS,
                        0.7F, 1.0F);
            }
            return new ItemStack(ModBlocks.CLAY_POT.get(), stack.getCount());
        }

        stack = stack.copy();
        stack.set(DataComponents.DAMAGE, damage);
        stack.set(ModDataComponentTypes.CLAY_POT_USAGE_COUNT.get(), new ClayPotDataComponent(this, Math.abs(damage - stack.getMaxDamage())));
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);

        if (!this.foodData.effects().isEmpty()) {
            TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
        }
    }
}

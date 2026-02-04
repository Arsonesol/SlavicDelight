package com.legomanchik.slavic_delight.mixin;

import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.JarDataComponent;
import com.legomanchik.slavic_delight.common.registry.ModDataComponentTypes;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void getTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (stack.has(ModDataComponentTypes.CLAY_POT_USAGE_COUNT.get())) {
            ClayPotDataComponent data = stack.get(ModDataComponentTypes.CLAY_POT_USAGE_COUNT.get());
            cir.setReturnValue(Optional.ofNullable(data));
            cir.cancel();
        }
        if (stack.has(ModDataComponentTypes.JAR_DATA.get())) {
            JarDataComponent data = stack.get(ModDataComponentTypes.JAR_DATA.get());
            if (!data.items().getFirst().is(Items.AIR)) {
                cir.setReturnValue(Optional.ofNullable(data));
                cir.cancel();
            }
        }
    }
}

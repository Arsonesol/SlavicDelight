package com.legomanchik.slavic_delight.common.registry;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.BrewBarrelMenu;
import com.legomanchik.slavic_delight.common.block.entity.container.menu.ClayPotMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, SlavicDelight.MOD_ID);

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    public static final Supplier<MenuType<ClayPotMenu>> CLAY_POT = MENUS
            .register("clay_pot", () -> IMenuTypeExtension.create(ClayPotMenu::new));
    public static final Supplier<MenuType<BrewBarrelMenu>> BREW_BARREL_MENU = MENUS.register("brew_barrel",
            () -> IMenuTypeExtension.create(BrewBarrelMenu::new));
}

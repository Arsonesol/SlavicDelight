package com.legomanchik.slavic_delight;

import com.legomanchik.slavic_delight.common.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(SlavicDelight.MOD_ID)
public class SlavicDelight {
    public static final String MOD_ID = "slavic_delight";

    public SlavicDelight(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModDataComponentTypes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        //modEventBus.addListener(VillageStructures::addNewVillageBuilding);
    }
}

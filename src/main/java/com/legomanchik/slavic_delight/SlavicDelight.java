package com.legomanchik.slavic_delight;

import com.legomanchik.slavic_delight.client.render.BrineJarRender;
import com.legomanchik.slavic_delight.client.render.JarItemsRender;
import com.legomanchik.slavic_delight.client.render.PicklesJarRender;
import com.legomanchik.slavic_delight.client.render.SauerkrautJarRender;
import com.legomanchik.slavic_delight.common.registry.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(SlavicDelight.MOD_ID)
public class SlavicDelight {
    public static final String MOD_ID = "slavic_delight";

    public SlavicDelight(IEventBus modEventBus, ModContainer modContainer) {
        //modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);

       //modEventBus.addListener(VillageStructures::addNewVillageBuilding);
    }

    @SubscribeEvent
    public void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(SlavicDelight::registerCompostables);
    }

    public static void registerCompostables() {
        // 10% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.CUCUMBER_SEEDS.get(), 0.1f);
        ComposterBlock.COMPOSTABLES.put(ModItems.RADISH_SEEDS.get(), 0.1f);

        // 30% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.BOILED_EGG.get(), 0.3f);

        // 50% chance
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILD_CUCUMBERS.get(), 0.5f);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILD_RADISH.get(), 0.5f);

        // 65% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.CUCUMBER.get(), 0.65f);
        ComposterBlock.COMPOSTABLES.put(ModItems.RADISH.get(), 0.65f);

        // 85% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.PICKLES.get(), 0.85f);
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
           //event.register(ModMenuTypes.BREW_BARREL_MENU.get(), BrewBarrelScreen::new);
           //event.register(ModMenuTypes.COOKING_POT_MENU.get(), CookingPotScreen::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.JAR_ENTITY.get(), JarItemsRender::new);
            event.registerBlockEntityRenderer(ModBlockEntities.PICKLES_JAR_ENTITY.get(), PicklesJarRender::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SAUERKRAUT_JAR_ENTITY.get(), SauerkrautJarRender::new);
            event.registerBlockEntityRenderer(ModBlockEntities.BRINE_JAR_ENTITY.get(), BrineJarRender::new);
        }
    }
}

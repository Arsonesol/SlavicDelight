package com.legomanchik.slavic_delight.common.event;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.client.gui.BrewBarrelScreen;
import com.legomanchik.slavic_delight.client.gui.ClayPotScreen;
import com.legomanchik.slavic_delight.client.gui.ClayPotTooltipComponent;
import com.legomanchik.slavic_delight.client.gui.JarTooltipComponent;
import com.legomanchik.slavic_delight.client.recipebook.RecipeCategories;
import com.legomanchik.slavic_delight.client.render.BrineJarRender;
import com.legomanchik.slavic_delight.client.render.JarItemsRender;
import com.legomanchik.slavic_delight.client.render.PicklesJarRender;
import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import com.legomanchik.slavic_delight.common.datacomponents.JarDataComponent;
import com.legomanchik.slavic_delight.common.registry.ModBlockEntities;

import com.legomanchik.slavic_delight.common.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

@EventBusSubscriber(modid = SlavicDelight.MOD_ID, value = Dist.CLIENT)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        RecipeCategories.init(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BREW_BARREL_MENU.get(), BrewBarrelScreen::new);
        event.register(ModMenuTypes.CLAY_POT.get(), ClayPotScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ClayPotDataComponent.class, ClayPotTooltipComponent::new);
        event.register(JarDataComponent.class, JarTooltipComponent::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.JAR_ENTITY.get(), JarItemsRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PICKLES_JAR_ENTITY.get(), PicklesJarRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BRINE_JAR_ENTITY.get(), BrineJarRender::new);
    }
}

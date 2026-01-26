package com.legomanchik.slavic_delight.client.render;


import com.legomanchik.slavic_delight.common.block.entity.BrineJarEntity;
import com.legomanchik.slavic_delight.common.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BrineJarRender implements BlockEntityRenderer<BrineJarEntity> {
    public BrineJarRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BrineJarEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.5f, 0.5f);
        itemRenderer.renderStatic(new ItemStack(ModBlocks.JAR.get()), ItemDisplayContext.FIXED, getLightLevel(Objects.requireNonNull(pBlockEntity.getLevel()), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();

        //FluidStack fluidStack = new FluidStack(Fluids.WATER,1000);
//
        //Level level = pBlockEntity.getLevel();
        //if (level == null)
        //    return;
//
        //float y = 0.2f;
        //float bottles = pBlockEntity.getBottles();
//
        //BlockPos pos = pBlockEntity.getBlockPos();
//
        //IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        //ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);
//
        //if (stillTexture == null)
        //    return;
//
        //FluidState state = fluidStack.getFluid().defaultFluidState();
//
        //TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
//
        //int waterColor = 0xC5DB64;
        //float r = (waterColor >> 16 & 255) / 255.0F;
        //float g = (waterColor >> 8 & 255) / 255.0F;
        //float b = (waterColor & 255) / 255.0F;
//
        //VertexConsumer builder = pBuffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
//
        //drawQuad(builder, pPoseStack, 0.25f, y * bottles, 0.25f, 0.75f, y * bottles, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, r, g, b);
        //pPoseStack.pushPose();
        //    pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        //pPoseStack.translate(-1f, 0, -1.5f);
//
        //drawQuad(builder, pPoseStack, 0.26f, 0, 0.76f, 0.76f, y * bottles, 0.76f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, r, g, b);
        //pPoseStack.popPose();
        //pPoseStack.pushPose();
        //pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
        //pPoseStack.translate(-1f, 0, 0);
//
        //drawQuad(builder, pPoseStack, 0.26f, 0, 0.26f, 0.76f, y * bottles, 0.26f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, r, g, b);
        //pPoseStack.popPose();
//
        //pPoseStack.pushPose();
        //pPoseStack.mulPose(Axis.YN.rotationDegrees(90));
        //pPoseStack.translate(0, 0, -1f);
        //drawQuad(builder, pPoseStack, 0.26f, 0, 0.26f, 0.76f, y * bottles, 0.26f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, r, g, b);
        //pPoseStack.popPose();
//
        //pPoseStack.pushPose();
        //pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
        //pPoseStack.translate(0f, 0, -0.5f);
        //drawQuad(builder, pPoseStack, 0.26f, 0, 0.76f, 0.76f, y * bottles, 0.76f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), pPackedLight, r, g, b);
        //pPoseStack.popPose();
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, float r, float g, float b) {
        builder.addVertex(poseStack.last().pose(), x, y, z)
                .setColor(r, g, b, 2F)
                .setUv(u, v)
                .setUv2(packedLight, packedLight)
                .setNormal(1, 0, 0);
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, float r, float g, float b) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, r, g, b);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, r, g, b);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, r, g, b);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, r, g, b);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

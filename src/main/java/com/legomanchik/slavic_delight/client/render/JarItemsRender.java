package com.legomanchik.slavic_delight.client.render;

import com.legomanchik.slavic_delight.common.block.entity.JarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;

public class JarItemsRender implements BlockEntityRenderer<JarBlockEntity> {
    public JarItemsRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(JarBlockEntity blockEntity, float partialtick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        float y = 0.1f;
        for(int i = 0; i < 3; i++) {
            ItemStack itemStack = blockEntity.getRenderStack(i);
            if(blockEntity.hasWater()) {
                y = 0.5f;
            }
            poseStack.pushPose();
            poseStack.translate(0.5f, y + (((float) i / 10) / 2), 0.5f);
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.mulPose(Axis.XP.rotationDegrees(270));

            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, buffer, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        if (blockEntity.hasWater()) {
            renderWater(blockEntity, poseStack, buffer, packedLight);
        }

    }

    private void renderWater(JarBlockEntity blockEntity, PoseStack poseStack,
                             MultiBufferSource buffer, int packedLight) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        if (level == null) return;

        FluidState waterState = Fluids.WATER.defaultFluidState();
        RenderType renderType = ItemBlockRenderTypes.getRenderLayer(waterState);

        TextureAtlasSprite waterSprite = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModelShaper()
                .getBlockModel(Blocks.WATER.defaultBlockState())
                .getParticleIcon();

        int waterColor = BiomeColors.getAverageWaterColor(level, pos);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

        float alpha = 0.8f;
        int r = (waterColor >> 16) & 0xFF;
        int g = (waterColor >> 8) & 0xFF;
        int b = waterColor & 0xFF;
        int a = (int) (alpha * 255);

        int color = (a << 24) | (r << 16) | (g << 8) | b;

        int sideColor = darkenColor(color, 0.7f);

        poseStack.pushPose();
        drawQuad(vertexConsumer, poseStack, 0.25f, 0.5f, 0.25f, 0.75f, 0.5f, 0.75f, waterSprite.getU0(), waterSprite.getV0(), waterSprite.getU1(), waterSprite.getV1(), packedLight, sideColor);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.translate(-1f, 0, -1.5f);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(-1f, 0, -1.5f);
        drawQuad(vertexConsumer, poseStack, 0.26f, 0, 0.76f, 0.76f, 0.5f, 0.76f, waterSprite.getU0(), waterSprite.getV0(), waterSprite.getU1(), waterSprite.getV1(), packedLight, sideColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-1f, 0, 0);
        drawQuad(vertexConsumer, poseStack, 0.26f, 0, 0.26f, 0.76f, 0.5f, 0.26f, waterSprite.getU0(), waterSprite.getV0(), waterSprite.getU1(), waterSprite.getV1(), packedLight, sideColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(90));
        poseStack.translate(0, 0, -1f);
        drawQuad(vertexConsumer, poseStack, 0.26f, 0, 0.26f, 0.76f, 0.5f, 0.26f, waterSprite.getU0(), waterSprite.getV0(), waterSprite.getU1(), waterSprite.getV1(), packedLight, sideColor);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(0));
        poseStack.translate(0f, 0, -0.5f);
        drawQuad(vertexConsumer, poseStack, 0.26f, 0, 0.76f, 0.76f, 0.5f, 0.76f, waterSprite.getU0(), waterSprite.getV0(), waterSprite.getU1(), waterSprite.getV1(), packedLight, sideColor);
        poseStack.popPose();
    }

    private int darkenColor(int color, float factor) {
        int a = (color >> 24) & 0xFF;
        int r = (int)(((color >> 16) & 0xFF) * factor);
        int g = (int)(((color >> 8) & 0xFF) * factor);
        int b = (int)((color & 0xFF) * factor);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private void drawQuad(VertexConsumer consumer, PoseStack poseStack,
                          float x1, float y1, float z1,
                          float x2, float y2, float z2,
                          float u1, float v1, float u2, float v2,
                          int packedLight, int color) {

        Matrix4f matrix = poseStack.last().pose();
        PoseStack.Pose normal = poseStack.last();

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(normal, 0, 1, 0);
        consumer.addVertex(matrix, x2, y1, z2).setColor(r, g, b, a).setUv(u2, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(normal, 0, 1, 0);
        consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(normal, 0, 1, 0);
        consumer.addVertex(matrix, x1, y2, z1).setColor(r, g, b, a).setUv(u1, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(normal, 0, 1, 0);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

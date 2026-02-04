package com.legomanchik.slavic_delight.client.gui;

import com.legomanchik.slavic_delight.common.datacomponents.JarDataComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;


public class JarTooltipComponent implements ClientTooltipComponent {
    private static final int SLOT_SIZE = 18;
    private static final int MARGIN = 2;
    private static final int BORDER = 1;

    private final NonNullList<ItemStack> items;
    private final boolean hasWater;

    public JarTooltipComponent(JarDataComponent data) {
        this.items = data.items();
        this.hasWater = data.hasWater();
    }

    @Override
    public int getHeight() {
        return SLOT_SIZE + BORDER * 2;
    }

    @Override
    public int getWidth(Font font) {
        return items.size() * SLOT_SIZE + (items.size() - 1) * MARGIN + BORDER * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (items.isEmpty()) return;

        int totalWidth = items.size() * SLOT_SIZE + (items.size() - 1) * MARGIN;

        if (hasWater) {
            int waterX = x + BORDER - 1;
            int waterY = y + BORDER - 1;
            int waterWidth = totalWidth;
            int waterHeight = SLOT_SIZE;

            guiGraphics.fill(waterX, waterY, waterX + waterWidth, waterY + waterHeight, 0x553D7DFF);

            for (int wave = 0; wave < 3; wave++) {
                int waveY = waterY + wave * 4;
                int waveAlpha = 30 - wave * 10;
                guiGraphics.fill(waterX, waveY, waterX + waterWidth, waveY + 1, (waveAlpha << 24) | 0x88CCFF);
            }
        }

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            int itemX = x + BORDER + i * (SLOT_SIZE + MARGIN);
            int itemY = y + BORDER;

            guiGraphics.renderItem(stack, itemX, itemY);
        }
    }
}

package com.legomanchik.slavic_delight.client.gui;

import com.legomanchik.slavic_delight.common.datacomponents.ClayPotDataComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClayPotTooltipComponent implements ClientTooltipComponent {
    private static final int SLOT_SIZE = 18;
    private static final int MARGIN = 2;
    private static final int BORDER = 1;

    private final int usageCount;
    private final Item pot;

    public ClayPotTooltipComponent(ClayPotDataComponent data) {
        this.pot = data.getPot();
        this.usageCount = data.getUsageCount();
    }

    @Override
    public int getHeight() {
        return SLOT_SIZE + BORDER * 2;
    }

    @Override
    public int getWidth(Font font) {
        return usageCount * SLOT_SIZE + (usageCount - 1) * MARGIN + BORDER * 2;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (usageCount == 0) return;

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < usageCount; i++) {
            items.add(new ItemStack(pot));
        }

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            int itemX = x + BORDER + i * (SLOT_SIZE + MARGIN);
            int itemY = y + BORDER;

            guiGraphics.renderItem(stack, itemX, itemY);
        }
    }
}

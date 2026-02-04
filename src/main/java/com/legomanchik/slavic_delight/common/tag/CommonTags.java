package com.legomanchik.slavic_delight.common.tag;

import com.legomanchik.slavic_delight.SlavicDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CommonTags {

    public static final TagKey<Item> BOTTLE = commonItemTag("bottle");

    private static TagKey<Block> commonBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, path));
    }

    private static TagKey<Item> commonItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, path));
    }

}
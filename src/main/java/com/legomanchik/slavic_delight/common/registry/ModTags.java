package com.legomanchik.slavic_delight.common.registry;


import com.legomanchik.slavic_delight.SlavicDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> CLAY_POT_BLOCK = tag("clay_pot");
        public static final TagKey<Block> JAR_BLOCK = tag("jar");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> BUCKET = tag("bucket");

        public static final TagKey<Item> BOTTLE = tag("bottle");

        public static final TagKey<Item> MAY_PLACE_TO_BREW_BARREL = tag("may_place_to_brew_barrel");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SlavicDelight.MOD_ID, name));
        }
    }
}
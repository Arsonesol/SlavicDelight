package com.legomanchik.slavic_delight.data;

import com.legomanchik.slavic_delight.SlavicDelight;
import com.legomanchik.slavic_delight.common.registry.ModItems;
import com.legomanchik.slavic_delight.common.tag.CommonTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider
{
    public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, SlavicDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerMinecraftTags();
        this.registerModTags();
    }

    private void registerMinecraftTags() {
    }

    private void registerModTags() {
        tag(CommonTags.BOTTLE).add(
                Items.GLASS_BOTTLE,
                Items.HONEY_BOTTLE,
                vectorwing.farmersdelight.common.registry.ModItems.MILK_BOTTLE.get(),
                ModItems.KVASS_BOTTLE.get(),
                ModItems.KEFIR_BOTTLE.get()
        );
    }
}
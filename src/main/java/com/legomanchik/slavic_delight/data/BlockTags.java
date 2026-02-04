package com.legomanchik.slavic_delight.data;

import com.legomanchik.slavic_delight.SlavicDelight;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider
{
    public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlavicDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
        this.registerForgeTags();
        this.registerCompatibilityTags();

        this.registerBlockMineables();
    }

    protected void registerBlockMineables() {
    }

    protected void registerForgeTags() {
    }

    protected void registerModTags() {
    }

    private void registerCompatibilityTags() {
    }
}

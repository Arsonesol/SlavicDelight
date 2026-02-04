package com.legomanchik.slavic_delight.client.recipebook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum BrewBarrelRecipeBookTab implements StringRepresentable {
    DRINKS("drinks");

    public static final Codec<BrewBarrelRecipeBookTab> CODEC = Codec.STRING.flatXmap(s -> {
        BrewBarrelRecipeBookTab tab = findByName(s);
        if (tab == null) {
            return DataResult.error(() -> "Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: " + EnumSet.allOf(BrewBarrelRecipeBookTab.class));
        }
        return DataResult.success(tab);
    }, tab -> DataResult.success(tab.toString()));

    public final String name;

    BrewBarrelRecipeBookTab(String name) {
        this.name = name;
    }

    public static BrewBarrelRecipeBookTab findByName(String name) {
        for (BrewBarrelRecipeBookTab value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}

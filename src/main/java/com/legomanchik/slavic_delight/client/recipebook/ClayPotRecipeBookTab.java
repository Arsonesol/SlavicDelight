package com.legomanchik.slavic_delight.client.recipebook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;

import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;

public enum ClayPotRecipeBookTab implements StringRepresentable {
    SOUPS("soups");

    public static final Codec<ClayPotRecipeBookTab> CODEC = Codec.STRING.flatXmap(s -> {
        ClayPotRecipeBookTab tab = findByName(s);
        if (tab == null) {
            return DataResult.error(() -> "Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: " + EnumSet.allOf(ClayPotRecipeBookTab.class));
        }
        return DataResult.success(tab);
    }, tab -> DataResult.success(tab.toString()));

    public final String name;

    ClayPotRecipeBookTab(String name) {
        this.name = name;
    }

    public static ClayPotRecipeBookTab findByName(String name) {
        for (ClayPotRecipeBookTab value : values()) {
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

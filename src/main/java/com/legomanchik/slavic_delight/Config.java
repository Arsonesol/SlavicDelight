package com.legomanchik.slavic_delight;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("Common Settings");

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

}

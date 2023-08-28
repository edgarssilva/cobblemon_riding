package com.edgarssilva.cobblemon_riding.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class RideablePokemonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> POKEMON_LIST;

    static {
        BUILDER.comment("List of pokemon that can be ridden");

        POKEMON_LIST = BUILDER.defineList("whitelist", new ArrayList<>(), (obj) -> obj instanceof String);

        SPEC = BUILDER.build();
    }
}

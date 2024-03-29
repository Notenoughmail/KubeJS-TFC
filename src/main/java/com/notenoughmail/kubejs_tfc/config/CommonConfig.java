package com.notenoughmail.kubejs_tfc.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CommonConfig {

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        register(builder);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build(), "kubejs-tfc.toml");
    }

    public static ForgeConfigSpec.BooleanValue debugMode;

    private static void register(ForgeConfigSpec.Builder builder) {
        builder.comment("KubeJS TFC common configuration");

        debugMode = builder.comment("""
                 If true:
                    Virtual json files created in the 'TFCEvents.data' and 'TFCEvents.worldgenData' events will be printed to the log
                    Important (and potentially unstable) events will be printed to the log
                 """)
                .define("debugMode", false);
    }
}

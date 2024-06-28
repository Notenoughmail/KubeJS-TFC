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
    public static ForgeConfigSpec.BooleanValue insertSelfTestsIntoKubeJSConsole;

    private static void register(ForgeConfigSpec.Builder builder) {
        builder.comment("KubeJS TFC common configuration");

        debugMode = builder.comment("""
                 If true, the following will be printed to the log:
                    Virtual json files created in the 'TFCEvents.data' and 'TFCEvents.worldgenData' events,
                    Important (and potentially unstable) events,
                    Block entity hijacking
                 """)
                .define("debugMode", false);

        insertSelfTestsIntoKubeJSConsole = builder.comment("""
                If true and TFC's datapack self tests are enabled, adds TFC's error message to the KubeJS log
                """)
                .define("insertSelfTestsIntoKubeJSConsole", true);
    }
}

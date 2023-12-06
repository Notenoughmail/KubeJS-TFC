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

    public static ForgeConfigSpec.BooleanValue disableAsyncRecipes;
    public static ForgeConfigSpec.BooleanValue debugMode;

    private static void register(ForgeConfigSpec.Builder builder) {
        builder.comment("KubeJS TFC common configuration");

        disableAsyncRecipes = builder.comment("""
                Automatically disables KubeJS' async recipes as it occasionally breaks TFC's knapping and alloying recipe types
                
                This only applies at instance start, using commands to reload KubeJS' common.properties file will set the value back to whatever is defined in the file
                
                This option, and the functionality it possesses, will be removed after TFC releases a version which safeguards against this (theoretically 3.1.3-beta)
                """)
                .define("disableAsyncRecipes", true);

        debugMode = builder.comment("""
                 If true:
                    Virtual json files created in the 'TFCEvents.data' and 'TFCEvents.worldgenData' events will be printed to the log
                    Important (and potentially unstable) events will be printed to the log
                 """)
                .define("debugMode", false);
    }
}

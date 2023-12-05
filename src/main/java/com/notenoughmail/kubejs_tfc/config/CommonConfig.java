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

    private static void register(ForgeConfigSpec.Builder builder) {
        builder.comment("KubeJS TFC common configuration");

        builder.comment("""
                        Automatically disables KubeJS' async recipes as it occasionally breaks TFC's knapping and alloying recipe types
                        
                        This only applies at instance start, using commands to reload KubeJS' common config will set the value back to whatever is defined in the file
                        """)
                .define("disableAsyncRecipes", true);
    }
}

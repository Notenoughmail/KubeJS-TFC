package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.TFCRecipeFilter;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import net.dries007.tfc.config.ConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MODID = "kubejs_tfc";

    public static final ForgeConfigSpec.Builder serverConfigBuilder = new ForgeConfigSpec.Builder();
    public static final ConfigBuilder wrappedServerConfigBuilder = new ConfigBuilder(serverConfigBuilder, "kubejs_tfc");


    public KubeJSTFC() {
    }

}
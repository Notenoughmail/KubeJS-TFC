package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.block.moss.*;
import com.notenoughmail.kubejs_tfc.recipe.schema.*;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.dries007.tfc.ForgeEventHandler;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.client.ClientEventHandler;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.SelfTests;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;

public class KubeJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void afterInit() {
        final ForgeConfigSpec spec = KubeJSTFC.serverConfigBuilder.build();

        if (!spec.isEmpty()) {
            ModList.get().getModContainerById(KubeJSTFC.MODID).ifPresent(container ->
                    container.addConfig(new ModConfig(ModConfig.Type.SERVER, spec, container, "kubejs-tfc-server.toml")));
        }
    }
}
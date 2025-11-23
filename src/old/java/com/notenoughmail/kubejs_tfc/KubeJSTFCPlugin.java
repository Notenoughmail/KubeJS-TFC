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

// Mild Javadoc abuse

/**
 * TODO: [Future]
 * <ul>
 *     <li>Blocks
 *         <ul>
 *             <li>Kinetics -- custom</li>
 *         </ul>
 *     </li>
 * 	   <li>Custom BiomeExtensions -- 1.21</li>
 * 	   <li><a href="https://discord.com/channels/303440391124942858/1279992681652682874">Attach TFC entity properties to existing entities, ping mo_mo</a>
 * 	   It may be possible to fudge this with attributes</li>
 * </ul>
 */
public class KubeJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void afterInit() {
        final ForgeConfigSpec spec = KubeJSTFC.serverConfigBuilder.build();

        if (!spec.isEmpty()) {
            ModList.get().getModContainerById(KubeJSTFC.MODID).ifPresent(container ->
                    container.addConfig(new ModConfig(ModConfig.Type.SERVER, spec, container, "kubejs-tfc-server.toml")));
        }
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        // KubeJS TFC
        filter.allow("com.notenoughmail.kubejs_tfc");
        filter.deny("com.notenoughmail.kubejs_tfc.util.implementation.mixin");
        filter.deny(KubeJSTFCPlugin.class);
        filter.deny("com.notenoughmail.kubejs_tfc.addons.precpros.PrecProsPlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.firmalife.FirmaLifePlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.afc.AFCPlugin");
        filter.deny("com.notenoughmail.kubejs_tfc.addons.tfccc.TFCCCPlugin");
        // TFC
        filter.allow("net.dries007.tfc");
        filter.deny("net.dries007.tfc.mixin");
        filter.deny("net.dries.tfc.network");
        filter.deny(SelfTests.class);
        filter.deny(ForgeEventHandler.class);
        filter.deny(InteractionManager.class);
        filter.deny(ClientEventHandler.class);
        filter.deny(ClientForgeEventHandler.class);
    }
}
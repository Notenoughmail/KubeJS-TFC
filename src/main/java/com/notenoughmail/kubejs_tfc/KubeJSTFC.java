package com.notenoughmail.kubejs_tfc;

import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import dev.latvian.mods.kubejs.CommonProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    public KubeJSTFC() {
        EventHandlers.init();
        CommonConfig.register();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandlers.init();
        }
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MODID, path);
    }
}
package com.notenoughmail.kubejs_tfc;

import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    public KubeJSTFC() {
       // ConsoleJS.CLIENT.setDebugEnabled(true);
    }
}